package bgu.spl.net.application.messages;

import bgu.spl.net.application.UserSession;

public class FollowMessage extends Message {

    private String username;

    public FollowMessage(String username){
        this.username=username;
    }

    public Message act(UserSession currentUserSession, Connections connection, HashMap<String,UserSession> usernameToUserSession){

        if(!currentUserSession.isLoggedIn() || !currentUserSession.addfollower(username))
            return new ErrorMessage();

        return new AckMessage();    //TODO:fix

    }

    
}
