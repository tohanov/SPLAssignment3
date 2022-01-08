package bgu.spl.net.impl.BGSServer;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.impl.BidiMessageEncoderDecoderImpl;
import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.srv.Server;


public class ReactorMain {
    public static void main(String[] args) {
        int numOfThreads =  Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        ConcurrentHashMap<String,UserSession> usernameToUserSession=new ConcurrentHashMap<>(); // TODO: need for concurrency?
        
        Server.reactor(
			numOfThreads, 
			port, 
			() -> new BidiMessagingProtocolImpl(usernameToUserSession) ,  
			() ->  new BidiMessageEncoderDecoderImpl()
		).serve();
      
        // Server.reactor(
        //         Runtime.getRuntime().availableProcessors(),
        //         7777, //port
        //         () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
        //         ObjectEncoderDecoder::new //message encoder decoder factory
        // ).serve();
    }
        
}
