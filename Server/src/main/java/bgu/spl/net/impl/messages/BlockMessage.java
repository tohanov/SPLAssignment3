package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class BlockMessage extends ClientToServerMessage{

    private String usernameToBlock;

    public BlockMessage(String usernameToBlock) {
        super(12);
        this.usernameToBlock = usernameToBlock;
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {
       
        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();

        if(currentUserSession==null /*|| !currentUserSession.isLoggedIn()*/)
            return error();

        UserSession userToBlock=usernameToUserSession.get(usernameToBlock); 
        
        if(
			userToBlock==null // if user to block doesn't exist
			|| currentUserSession.getUsername().equals(usernameToBlock) // can't block yourself
			|| currentUserSession.isBlockingOtherUser(usernameToBlock)) // if already blocking that user
            return error();

        currentUserSession.blockUser(userToBlock);

        return ack();
       
    }

    

    
    
}
