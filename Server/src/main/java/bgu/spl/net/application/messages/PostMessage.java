import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import bgu.spl.net.application.UserSession;

public class PostMessage extends Message{

    private String messageToPost;
    private Set<String> targetUsers;

    public PostMessage(String messageToPost, ArrayList<String> _targetUsers){
        this.messageToPost=messageToPost;
        targetUsers=new HashSet<String>(_targetUsers);
    }

    public Message act(UserSession currentUserSession, Connections connections, HashMap<String,UserSession> usernameToUserSession){
        if(currentUserSession==null /* || !currentUserSession.isLoggedIn() */)
            return new ErrorMessage();
        
        else{
            targetUsers.addAll(currentUserSession.getFollowers());  //FIXME: check concurrency: user unfollows during the function
            targetUsers.remove(currentUserSession.getUsername);

            NotificationMessage wrappedMessage = new NotificationMessage(messageToPost);
            byte[] encodedMessage=EncDec.encode(wrappedMessage);
            for (String username : targetUsers) {
                
                UserSession targetUser=usernameToUserSession.get(username);

                if(targetUser.isLoggedIn)           //FIXME: concurrency: user logouts during sending
                    connections.getHandler(targetUser.getSessionId()).send(encodedMessage); 
                else{
                    targetUser.getReceivedMessages().add(encodedMessage);
                }
                
            }
            
            //FIXME: check if not sending message to self, check if blocked, archive the messages 
            return new AckMessage();
        }

    }


    
}
