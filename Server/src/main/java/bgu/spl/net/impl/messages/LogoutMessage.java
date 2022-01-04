package bgu.spl.net.impl.messages;

import java.util.HashMap;

import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.messages.AckMessage;

public class LogoutMessage extends ClientToServerMessage {

    public LogoutMessage(){
        super(3);
    }

    @Override
    public ServerToClientMessage act(Integer id, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //TODO: Please notice that LOGOUT message closes the client's program.
        UserSession currentnUserSession = connections.getHandler(id).getUserSession();
        if(currentnUserSession==null)
            return error();

        connections.getHandler(id).setUserSession(null);
        currentnUserSession.resetSessionId();
        return ack();



    }
    
}
