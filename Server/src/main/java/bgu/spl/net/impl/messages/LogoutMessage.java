package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class LogoutMessage extends ClientToServerMessage {

    public LogoutMessage(){
        super(3);
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {
        //TODO: Please notice that LOGOUT message closes the client's program.
        UserSession currentnUserSession = connections.getHandler(currentUserId).getUserSession();

        if(currentnUserSession==null)
            return error();

        connections.getHandler(currentUserId).setUserSession(null);
        currentnUserSession.resetSessionId();
        return ack();



    }
    
}
