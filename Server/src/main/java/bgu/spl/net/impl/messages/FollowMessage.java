package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class FollowMessage extends ClientToServerMessage {

    private String usernameToFollow;
    private UserSession userSessionToFollow;


    public FollowMessage(String usernameTOFollow){
        super(4);
        this.usernameToFollow=usernameTOFollow;

    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession){

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();
        userSessionToFollow=usernameToUserSession.get(usernameToFollow);

        if(!currentUserSession.isLoggedIn() || userSessionToFollow==null ||
            userSessionToFollow.isBlockingOtherUser(currentUserSession.getUsername()) ||!userSessionToFollow.addfollower(currentUserSession))
                return error();

        currentUserSession.increaseFollowing();    
        return ack(usernameToFollow);    //TODO:fix

    }

    
}
