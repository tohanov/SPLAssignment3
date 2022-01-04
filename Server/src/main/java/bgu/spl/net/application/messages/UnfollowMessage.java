import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.ClientToServerMessage;

public class UnfollowMessage extends ClientToServerMessage{
    
    private String usernameToUnfollow;
    private UserSession userSessionToUnfollow;

    public UnfollowMessage(String username){
        super(4);
        this.usernameToUnfollow=username;

    }

    public ServerToClientMessage act(UserSession currentUserSession, Connections connection, HashMap<String,UserSession> usernameToUserSession){

        userSessionToUnfollow=usernameToUserSession.get(usernameToUnfollow);

        if(!currentUserSession.isLoggedIn() || userSessionToUnfollow==null || !userSessionToUnfollow.removeFollower(currentUserSession.getUsername()));
            return error();

        currentUserSession.decreaseFollowing();    
        return ack(); 

    }
}
