package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.ConnectionHandler;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.ConnectionHandlerImpl;
import bgu.spl.net.impl.ConnectionsImpl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.function.Supplier;


public abstract class BaseServer<T> implements Server<T> {
    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;

    private Connections<T> connections;
    private int handlerIdCounter;
    private ServerSocket sock;

    
    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory){

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;

		handlerIdCounter = 0;
		connections = new ConnectionsImpl<>();
    }


    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSock = serverSock.accept();

				ConnectionHandler<T> handler = new ConnectionHandlerImpl<T>(
                    handlerIdCounter,
                    connections,
                    clientSock,
                    protocolFactory.get(),
                    encdecFactory.get()
				);

				// System.out.println("[*] inside serve, before setHandler");
                // tempProtocol.setHandler(handler); // FIXME : see if needed
                // tempProtocol.start(handlerIdCounter++, connections);
				
				((ConnectionsImpl<T>)connections).addIdToConnections(handlerIdCounter++, handler);
                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }


    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }


    protected abstract void execute(ConnectionHandler<T>  handler);
}
