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
    public ServerToClientMessage act(int connectionId, /* UserSession currentUserSession, */ Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //synchronized(usersession.getreceivedMessages???)

        // TODO: check for waiting messages from when was logged off

        // if logged in then error
        // if not registered then error
        // if password is wrong then error
        // else allow login
            // set the connection id of the user session of the given username to the one of the connection handler
            // 

        UserSession userSession = usernameToUserSession.get(username);
        if (userSession == null || !password.equals(userSession.getPassword()) || userSession.setSessionId( connectionId ) ) {
            return error();
        }

        // connections.get(connectionId) //FIXME
        
        return ack();
    }

    
    
}
