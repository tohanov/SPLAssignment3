package bgu.spl.net.application.messages;

import java.sql.Connection;
import java.util.HashMap;

import bgu.spl.net.application.UserSession;

public class LoginMessage extends ClientToServerMessage {

    private String username;
    private String password;
   
    public LoginMessage(String username, String password, String birthday){
        super(2);
        this.username=username;
        this.password=password;
              
    }

    @Override
    public ServerToClientMessage act(Integer connectionId, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //synchronized(usersession.getreceivedMessages???)
        UserSession currentUserSession=usernameToUserSession.get(username);

        if(currentUserSession==null|| !password.equals(currentUserSession.getPassword()) || currentUserSession.setSessionId( connectionId ))
            return new ErrorMessage();

            
        return new AckMessage(2);
        // TODO: check for waiting messages from when was logged off
    }

    
    
}
