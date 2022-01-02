package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.application.UserSession;

public class FollowMessage extends ClientToServerMessage {

    private String usernameToFollow;
    private UserSession userSessionToFollow;


    public FollowMessage(String usernameTOFollow){
        super(4);
        this.usernameToFollow=usernameTOFollow;
    }

    public ClientToServerMessage act(UserSession currentUserSession, Connections connection, HashMap<String,UserSession> usernameToUserSession){

        userSessionToFollow=usernameToUserSession.get(usernameToFollow);

        if(!currentUserSession.isLoggedIn() || userSessionToFollow==null || !userSessionToFollow.addfollower(currentUserSession.getUsername()))
            return new ErrorMessage();

        currentUserSession.increaseFollowing();    
        return new AckMessage();    //TODO:fix

    }

    
}
