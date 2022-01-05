package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class LoginMessage extends ClientToServerMessage {

    private String username;
    private String password;
    private byte captcha;
   
    public LoginMessage(String username, String password, byte captcha){
        super(2);
        this.username=username;
        this.password=password;
        this.captcha=captcha;
              
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {
        //synchronized(usersession.getreceivedMessages???)

        // TODO: check for waiting messages from when was logged off

        // if logged in then error
        // if not registered then error
        // if password is wrong then error
        // else allow login
            // set the connection id of the user session of the given username to the one of the connection handler
            // 

        UserSession userSession = usernameToUserSession.get(username);
        if (userSession == null || captcha!=1 || !password.equals(userSession.getPassword()) || userSession.setSessionId(currentUserId) ) {
            return error();
        }

        // connections.get(connectionId) //FIXME

        for(ServerToClientMessage unreadMessage : userSession.getReceivedMessages()){
            connections.getHandler(currentUserId).send(unreadMessage);
            userSession.getReceivedMessages().remove(unreadMessage);
        }
        
        return ack();
    }

    
    
}
