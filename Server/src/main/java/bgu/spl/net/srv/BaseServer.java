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

    private Connections<T> connections = new ConnectionsImpl<>();
    private int id;
    private ServerSocket sock;

    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory){

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();
                BidiMessagingProtocol<T> tempProtocol = protocolFactory.get();
                ConnectionHandler<T> handler = new ConnectionHandlerImpl<T>(
                    id,
                    connections,
                    clientSock,
                    tempProtocol,
                    encdecFactory.get()
                    );
                tempProtocol.setHandler(handler);
                tempProtocol.start(id, connections);

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
