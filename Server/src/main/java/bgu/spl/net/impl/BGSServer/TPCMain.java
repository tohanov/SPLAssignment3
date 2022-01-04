import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.ServerToClientMessage;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);
        int numOfThreads =  Integer.parseInt(args[1]);

        Server.threadPerClient(port, () -> new BidiMessagingProtocolImpl<>(usernameToUserSession) ,  () ->  new BidiMessageEncoderDecoderImpl<>()).serve();;
        
        
    }
    
}
