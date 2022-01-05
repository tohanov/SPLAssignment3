package bgu.spl.net.impl.messages;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class UnfollowMessage extends ClientToServerMessage{
    
    private String usernameToUnfollow;
    private UserSession userSessionToUnfollow;

    public UnfollowMessage(String username){
        super(4);
        this.usernameToUnfollow=username;

    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession){

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();
        userSessionToUnfollow=usernameToUserSession.get(usernameToUnfollow);

        if(!currentUserSession.isLoggedIn() || userSessionToUnfollow==null || !userSessionToUnfollow.removeFollower(currentUserSession))
            return error();

        //currentUserSession.decreaseFollowing();    
        return ack(); 

    }
}
