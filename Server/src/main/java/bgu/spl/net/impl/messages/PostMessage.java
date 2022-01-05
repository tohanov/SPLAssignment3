package bgu.spl.net.impl.messages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class PostMessage extends ClientToServerMessage{

    private String messageToPost;
    private Set<String> targetUsers;

    public PostMessage(String messageToPost, ArrayList<String> targetUsers){
        super(5);
        this.messageToPost=messageToPost;
        this.targetUsers=new HashSet<String>(targetUsers);
    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession){

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();

        if(currentUserSession==null /* || !currentUserSession.isLoggedIn() */)
            return error();
        
        else{
            

            targetUsers.addAll(currentUserSession.getFollowers());  //FIXME: check concurrency: user unfollows during the function
            targetUsers.remove(currentUserSession.getUsername());
            targetUsers.removeAll(currentUserSession.getBlockedUsers());

            NotificationMessage wrappedMessage = new NotificationMessage(5,currentUserSession.getUsername(),messageToPost);
                        
            for (String username : targetUsers) {
                
                UserSession targetUserSession=usernameToUserSession.get(username);

                if(targetUserSession != null && !targetUserSession.isBlockingOtherUser(currentUserSession.getUsername())){

                    targetUserSession.increaseNumOfPosts();

                    if(targetUserSession.isLoggedIn())           //FIXME: concurrency: user logouts during sending
                        connections.getHandler(targetUserSession.getSessionId()).send(wrappedMessage); 
                    else{

                        targetUserSession.getReceivedMessages().add(wrappedMessage);
                    }
                }
                
            }
            
            //FIXME: check if not sending message to self, check if blocked, archive the messages 
            return ack();
        }

    }


    
}
