package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.ConnectionHandler;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class NonBlockingConnectionHandler<T> implements ConnectionHandler<T> {
    private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
    private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    private final SocketChannel chan;
    private final Reactor<T> reactor;
	private Connections<T> connections;
	private int handlerId;
	private UserSession userSession;


    public NonBlockingConnectionHandler(
            MessageEncoderDecoder<T> reader,
            BidiMessagingProtocol<T> protocol,
            SocketChannel chan,
            Reactor<T> reactor,
			Connections<T> connections,
			int id
			) {
        this.chan = chan;
        this.encdec = reader;
        this.protocol = protocol;
        this.reactor = reactor;

		
        this.handlerId = id;
		this.connections = connections;
		this.userSession = null;
    }


    public Runnable continueRead() {
		// buffer to read into
        ByteBuffer buf = leaseBuffer();

        boolean success = false;
        try {
            success = chan.read(buf) != -1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (success) {
			// prepare buffer to be read from setting the position at 0 and limit at the last written/read byte
            buf.flip();

			// prepare a runnable task for the server to run
            return () -> {
                try {
					protocol.start(handlerId,connections);

                    while (buf.hasRemaining()) {
                        T nextMessage = encdec.decodeNextByte(buf.get());
                        if (nextMessage != null) {
                            // T response = protocol.process(nextMessage); //FIXME process is void
                            // if (response != null) {
                            //     writeQueue.add(ByteBuffer.wrap(encdec.encode(response)));
                            //     reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            // }

							protocol.process(nextMessage);
                        }
                    }
                } finally {
                    releaseBuffer(buf);
                }
            };
        } else {
            releaseBuffer(buf);
            close();
            return null;
        }
    }


    public void close() {
        try {
            chan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public boolean isClosed() {
        return !chan.isOpen();
    }


    public void continueWrite() {
        while (!writeQueue.isEmpty()) {
            try {
                ByteBuffer top = writeQueue.peek();
                chan.write(top);
                if (top.hasRemaining()) {
                    return;
                } else {
                    writeQueue.remove();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
            }
        }

        if (writeQueue.isEmpty()) {
            if (protocol.shouldTerminate()) close();
            else reactor.updateInterestedOps(chan, SelectionKey.OP_READ);
        }
    }


    private static ByteBuffer leaseBuffer() {
        ByteBuffer buff = BUFFER_POOL.poll();

		// if used all buffers in pool
        if (buff == null) {
			// this new buffer will be added to the pool when done with
            return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
        }

		// discard previous usage info
        buff.clear();

        return buff;
    }


    private static void releaseBuffer(ByteBuffer buff) {
		// put unneeded used buffer in the pool for future usage by other threads
        BUFFER_POOL.add(buff);
    }


	@Override
	public void send(T msg) {
		// TODO Auto-generated method stub
		// FIXME

		// encode servertoclient message
		// add to writeQueue

		
		writeQueue.add(ByteBuffer.wrap(encdec.encode(msg)));
		reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}


	@Override
	public UserSession getUserSession() {
		return userSession;
	}


	@Override
	public void setUserSession(UserSession session) {
		userSession = session;
	}

}
