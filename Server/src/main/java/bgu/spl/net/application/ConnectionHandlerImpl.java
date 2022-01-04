package bgu.spl.net.application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;



public class ConnectionHandlerImpl<T> implements ConnectionHandler<T>,Runnable{
    private final BidiMessagingProtocol<T> protocol;
    private final BidiMessageEncoderDecoderImpl encDec;
    private final int id;
    private Socket socket;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private UserSession userSession;
    
   

    public ConnectionHandlerImpl(int _id, Connections connections, HashMap<String,UserSession> usernameToSession, Socket socket, BidiMessagingProtocol<T> protocol, BidiEncoderDecoder<T> encDec){
        id = _id;
        this.socket=socket;
        this.protocol=protocol;
        this.protocol.start(id, connections);
        
        this.encDec=encDec;   
        try {
            in=new BufferedInputStream(socket.getInputStream());
            out=new BufferedOutputStream(socket.getOutputStream()); 
        } catch (IOException e) {
            e.printStackTrace();
        }
           
        userSession=null;
    }

    @Override
    public void close() throws IOException {
        socket.close();        
    }

    @Override
    public void send(T msg) {
        try{
            out.write(encDec.encode(msg));
            out.flush();

        } catch(IOException e){
            e.printStackTrace();
        }
        
        
        
    }

    @Override
    public void run() {
        
        int read;
        try {
            while (!protocol.shouldTerminate() && (read = in.read()) >= 0) {
                T nextMessage = encDec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    // T response = protocol.process(nextMessage);
                    // if (response != null) {
                    //     out.write(enDdec.encode(response));
                    //     out.flush();
                    // }

                    protocol.process(nextMessage);
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public UserSession getSession(){
        return userSession;
    }
    public void setUserSession(UserSession newUserSession){
        userSession=newUserSession;
    }
    
}
