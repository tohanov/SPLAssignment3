package bgu.spl.net.application.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import bgu.spl.net.application.UserSession;

public class PostMessage extends ClientToServerMessage{

    private String messageToPost;
    private Set<String> targetUsers;

    public PostMessage(String messageToPost, ArrayList<String> targetUsers){
        super(5);
        this.messageToPost=messageToPost;
        this.targetUsers=new HashSet<String>(targetUsers);
    }

    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String,UserSession> usernameToUserSession){
        if(currentUserSession==null /* || !currentUserSession.isLoggedIn() */)
            return error();
        
        else{
            

            targetUsers.addAll(currentUserSession.getFollowers());  //FIXME: check concurrency: user unfollows during the function
            targetUsers.remove(currentUserSession.getUsername());
            targetUsers.removeAll(currentUserSession.getBlockedUsers());

            NotificationMessage wrappedMessage = new NotificationMessage(5,currentUserSession.getUsername(),messageToPost);
            Byte[] encodedMessage=EncDec.encode(wrappedMessage);
            
            for (String username : targetUsers) {
                
                UserSession targetUserSession=usernameToUserSession.get(username);

                if(targetUserSession != null && !targetUserSession.isBlockingOtherUser(currentUserSession.getUsername())){

                    targetUserSession.increaseNumOfPosts();

                    if(targetUserSession.isLoggedIn())           //FIXME: concurrency: user logouts during sending
                        connections.getHandler(targetUserSession.getSessionId()).send(encodedMessage); 
                    else{

                        targetUserSession.getReceivedMessages().add(encodedMessage);
                    }
                }
                
            }
            
            //FIXME: check if not sending message to self, check if blocked, archive the messages 
            return ack();
        }

    }


    
}
