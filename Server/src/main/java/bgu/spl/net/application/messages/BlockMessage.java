package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.application.UserSession;

public class BlockMessage implements ClientToServerMessage{

    private String usernameToBlock;

    public BlockMessage(String userToBlock) {
        this.usernameToBlock = userToBlock;
    }

    @Override
    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        //TODO: take in cosideration in follow and post and PM

        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return new ErrorMessage();

        UserSession userToBlock=usernameToUserSession.get(userToBlock); 
        
        if(userToBlock==null || currentUserSession.isBlockingOtherUser(usernameToBlock))
            return new ErrorMessage();

        currentUserSession.blockUser(userToBlock);

        return new AckMessage(12); 

        
        




       
    }

    

    
    
}
