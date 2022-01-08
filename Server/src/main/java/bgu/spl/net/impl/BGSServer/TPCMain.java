package bgu.spl.net.impl.BGSServer;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.impl.BidiMessageEncoderDecoderImpl;
import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.srv.Server;


public class TPCMain {
    public static void main(String[] args) {
		// System.out.println(Arrays.deepToString(args));

        int port = Integer.parseInt(args[0]);
      
        ConcurrentHashMap<String,UserSession> usernameToUserSession=new ConcurrentHashMap<>(); // TODO: need for concurrency?

        Server.threadPerClient(
            port,
            () -> new BidiMessagingProtocolImpl(usernameToUserSession),
            () -> new BidiMessageEncoderDecoderImpl()
        ).serve();
    }
}
