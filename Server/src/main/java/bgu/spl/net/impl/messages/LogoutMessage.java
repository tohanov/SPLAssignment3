package bgu.spl.net.impl.messages;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.ConnectionHandler;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.ConnectionHandlerImpl;
import bgu.spl.net.impl.UserSession;

public class LogoutMessage extends ClientToServerMessage {

    public LogoutMessage(){
        super(3);
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {
        //TODO: Please notice that LOGOUT message closes the client's program.
		
		ConnectionHandlerImpl<Message> handler = connections.getHandler(currentUserId);
        UserSession currentnUserSession = handler.getUserSession();

        if(currentnUserSession==null)
            return error();

		handler.setUserSession(null);
        currentnUserSession.resetSessionId();

        return ack();
    }
    
}
