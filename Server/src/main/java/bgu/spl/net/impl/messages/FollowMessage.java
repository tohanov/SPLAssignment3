package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class FollowMessage extends ClientToServerMessage {

    private String usernameToFollow;
    // private UserSession userSessionToFollow;


    public FollowMessage(String usernameTOFollow){
        super(4);
        this.usernameToFollow=usernameTOFollow;

    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession){

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();
        UserSession userSessionToFollow=usernameToUserSession.get(usernameToFollow);

        if(
			currentUserSession == null // if current connection is not logged in
			|| userSessionToFollow==null // if the user to be followed doesn't exist
			|| currentUserSession.getUsername().equals(usernameToFollow) // can't follow yourself
			|| userSessionToFollow.isBlockingOtherUser(currentUserSession.getUsername()) // blocked user can't follow and blocking user can't follow
			// || currentUserSession.isBlockingOtherUser(userSessionToFollow.getUsername())
			|| !userSessionToFollow.addfollower(currentUserSession)
		) {
                return error();
		}

        currentUserSession.increaseFollowing();
        return ack('\0' + usernameToFollow);

    }

    
}
