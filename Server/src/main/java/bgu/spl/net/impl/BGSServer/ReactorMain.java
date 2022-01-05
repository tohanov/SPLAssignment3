package bgu.spl.net.impl.BGSServer;

import java.util.function.Supplier;

import bgu.spl.net.srv.Server;

public class ReactorMain {


    public static void main(String[] args) {
        // int port = Integer.parseInt(args[0]);
        // int numOfThreads =  Integer.parseInt(args[1]);
        
        // Server.reactor(numOfThreads, port, () -> new BidiMessagingProtocolImpl<>() ,  () ->  new BidiMessageEncoderDecoderImpl<>()).serve();
       //------------------------------------ 
        // Server.reactor(
        //         Runtime.getRuntime().availableProcessors(),
        //         7777, //port
        //         () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
        //         ObjectEncoderDecoder::new //message encoder decoder factory
        // ).serve();

        
    }
        
}
