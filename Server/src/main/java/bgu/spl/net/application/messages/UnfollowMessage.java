import java.util.HashMap;

import bgu.spl.net.application.UserSession;

public class UnfollowMessage {
    
    private String usernameToUnfollow;
    private UserSession userSessionToUnfollow;

    public UnfollowMessage(String username){
        this.usernameToUnfollow=username;

    }

    public ClientToServerMessage act(UserSession currentUserSession, Connections connection, HashMap<String,UserSession> usernameToUserSession){

        userSessionToUnfollow=usernameToUserSession.get(usernameToUnfollow);

        if(!currentUserSession.isLoggedIn() || userSessionToUnfollow==null || !userSessionToUnfollow.removeFollower(currentUserSession.getUsername()));
            return new ErrorMessage();

        currentUserSession.decreaseFollowing();    
        return new AckMessage();    

    }
}
