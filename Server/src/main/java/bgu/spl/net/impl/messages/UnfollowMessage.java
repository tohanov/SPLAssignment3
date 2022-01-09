package bgu.spl.net.impl.messages;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.ConnectionsImpl;

public class UnfollowMessage extends ClientToServerMessage{
    
    private String usernameToUnfollow;

    public UnfollowMessage(String username){
        super(4);
        this.usernameToUnfollow=username;

    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession){

        UserSession currentUserSession = ((ConnectionsImpl<Message>)connections).getHandler(currentUserId).getUserSession();
        UserSession userSessionToUnfollow=usernameToUserSession.get(usernameToUnfollow);

        if(
			currentUserSession == null // if not logged in
			|| userSessionToUnfollow==null // if the requested user to follow doesn't exist
			|| !userSessionToUnfollow.removeFollower(currentUserSession) // if wan't following the user from the get go
		) 
            return error();

        //currentUserSession.decreaseFollowing();    
        return ack('\1' + usernameToUnfollow); 

    }
}
