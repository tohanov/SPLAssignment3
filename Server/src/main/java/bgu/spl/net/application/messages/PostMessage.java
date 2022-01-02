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
        this.messageToPost=messageToPost;
        this.targetUsers=new HashSet<String>(targetUsers);
    }

    public ClientToServerMessage act(UserSession currentUserSession, Connections connections, HashMap<String,UserSession> usernameToUserSession){
        if(currentUserSession==null /* || !currentUserSession.isLoggedIn() */)
            return new ErrorMessage();
        
        else{
            

            targetUsers.addAll(currentUserSession.getFollowers());  //FIXME: check concurrency: user unfollows during the function
            targetUsers.remove(currentUserSession.getUsername());

            NotificationMessage wrappedMessage = new NotificationMessage(messageToPost);
            Byte[] encodedMessage=EncDec.encode(wrappedMessage);
            for (String username : targetUsers) {
                
                UserSession targetUserSession=usernameToUserSession.get(username);

                if(targetUserSession != null){

                    targetUserSession.increaseNumOfPosts();

                    if(targetUserSession.isLoggedIn())           //FIXME: concurrency: user logouts during sending
                        connections.getHandler(targetUserSession.getSessionId()).send(encodedMessage); 
                    else{

                        targetUserSession.getReceivedMessages().add(encodedMessage);
                    }
                }
                
            }
            
            //FIXME: check if not sending message to self, check if blocked, archive the messages 
            return new AckMessage(5);
        }

    }


    
}
