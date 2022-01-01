package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.AckMessage;

public class LogoutMessage extends Message {

    @Override
    public Message act(Integer id, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        
        UserSession currentnUserSession = connections.getHandler(id).getSession();
        if(currentnUserSession==null)
            return new ErrorMessage();

        connections.getHandler(id).setUserSession(null);
        currentnUserSession.resetSessionId();
        return new AckMessage();



    }
    
}
