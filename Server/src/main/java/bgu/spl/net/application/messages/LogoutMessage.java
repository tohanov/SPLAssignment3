package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.AckMessage;

public class LogoutMessage extends ClientToServerMessage {

    @Override
    public ClientToServerMessage act(Integer id, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //TODO: Please notice that LOGOUT message closes the client's program.
        UserSession currentnUserSession = connections.getHandler(id).getSession();
        if(currentnUserSession==null)
            return new ErrorMessage();

        connections.getHandler(id).setUserSession(null);
        currentnUserSession.resetSessionId();
        return ack();



    }
    
}
