package bgu.spl.net.impl.messages;

import java.util.HashMap;

import bgu.spl.net.impl.UserSession;

public class BlockMessage extends ClientToServerMessage{

    private String usernameToBlock;

    public BlockMessage(String userToBlock) {
        super(12);
        this.usernameToBlock = userToBlock;
    }

    @Override
    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
       
        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        UserSession userToBlock=usernameToUserSession.get(userToBlock); 
        
        if(userToBlock==null || currentUserSession.isBlockingOtherUser(usernameToBlock))
            return error();

        currentUserSession.blockUser(userToBlock);

        return ack();
       
    }

    

    
    
}
