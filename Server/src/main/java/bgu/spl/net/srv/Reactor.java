package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.ConnectionsImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;


public class Reactor<T> implements Server<T> {
    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> readerFactory;
    private final ActorThreadPool pool;
    private Selector selector;

    private Thread selectorThread;
    private final ConcurrentLinkedQueue<Runnable> selectorTasks = new ConcurrentLinkedQueue<>();

	private Connections<T> connections;
	private int handlerIdCounter;


    public Reactor(
            int numThreads,
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> readerFactory) {

        this.pool = new ActorThreadPool(numThreads);
        this.port = port;
        this.protocolFactory = protocolFactory;
        this.readerFactory = readerFactory;

		connections = new ConnectionsImpl<>();
		handlerIdCounter = 0;
    }

	
    @Override
    public void serve() {
		selectorThread = Thread.currentThread();

        try (
			Selector selector = Selector.open();
            ServerSocketChannel serverSock = ServerSocketChannel.open()
		) {
            this.selector = selector; //just to be able to close

            serverSock.bind(new InetSocketAddress(port));
            serverSock.configureBlocking(false); // make thread non blocking

			// make selector notify of connections to port that are ready to be accepted
            serverSock.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("Server started");

            while (!Thread.currentThread().isInterrupted()) {
                selector.select(); // blocks untill there's stuff to do
                runSelectionThreadTasks(); // run all tasks returned by connection handlers

				// go over selector piled up notifications
                for (SelectionKey key : selector.selectedKeys()) {
                    if (!key.isValid()) {
                        continue;
                    } else if (key.isAcceptable()) { // if it's a connection ready to be accepted
                        handleAccept(serverSock, selector);
                    } else {
                        handleReadWrite(key);
                    }
                }

				//clear the selected keys set so that we can know about new events
                selector.selectedKeys().clear(); 
            }

        } catch (ClosedSelectorException ex) {
            //do nothing - server was requested to be closed
        } catch (IOException ex) {
            //this is an error
            ex.printStackTrace();
        }

        System.out.println("server closed!!!");
        pool.shutdown();
    }


    /*package*/ void updateInterestedOps(SocketChannel chan, int ops) {
		// update what to wake the server up for, regarding this channel

        final SelectionKey key = chan.keyFor(selector);
        if (Thread.currentThread() == selectorThread) {
			// only the server thread touches the selector
            key.interestOps(ops);
        } else {
			// make server thread update ops
            selectorTasks.add(() -> {
                key.interestOps(ops);
            });
            selector.wakeup();
        }
    }


    private void handleAccept(ServerSocketChannel serverChan, Selector selector) throws IOException {
		// create a new handler for the accepted connection and register it in the selector for reading event

        SocketChannel clientChan = serverChan.accept();
        clientChan.configureBlocking(false); // nonblocking handler needs nonblocking socket

        final NonBlockingConnectionHandler<T> handler = new NonBlockingConnectionHandler<>(
			readerFactory.get(),
			protocolFactory.get(),
			clientChan,
			this,
			connections,
			handlerIdCounter
		);

		((ConnectionsImpl<T>)connections).addIdToConnections(handlerIdCounter++, handler);
        clientChan.register(selector, SelectionKey.OP_READ, handler);
    }


    private void handleReadWrite(SelectionKey key) {
		// server was woken up by the selector because it needs to handle communication with existing clients

		// get handler which corresponds to the relevant client
        @SuppressWarnings("unchecked")
        NonBlockingConnectionHandler<T> handler = (NonBlockingConnectionHandler<T>) key.attachment();

        if (key.isReadable()) {
            Runnable task = handler.continueRead();
			
            if (task != null) {
                pool.submit(handler, task);
            }
        }

	    if (key.isValid() && key.isWritable()) {
            handler.continueWrite();
        }
    }

    private void runSelectionThreadTasks() {
        while (!selectorTasks.isEmpty()) {
            selectorTasks.remove().run();
        }
    }

    @Override
    public void close() throws IOException {
        selector.close();
    }
}
