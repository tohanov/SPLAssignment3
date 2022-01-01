package bgu.spl.net.application.messages;

import java.sql.Connection;
import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import messages.Message;

public class LoginMessage extends Message {

    private String username;
    private String password;
   
    public LoginMessage(String username, String password, String birthday){
        this.username=username;
        this.password=password;
              
    }

    @Override
    public Message act(Integer connectionId, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //synchronized(usersession.getreceivedMessages???)
        UserSession currentUserSession=usernameToUserSession.get(username);

        if(currentUserSession==null|| !password.equals(currentUserSession.getPassword()) || currentUserSession.setSessionId( connectionId ))
            return new ErrorMessage();

            
        return new AckMessage();
        // TODO: check for waiting messages from when was logged off
    }

    
    
}
