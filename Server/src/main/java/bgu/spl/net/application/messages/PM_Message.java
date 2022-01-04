import java.util.HashMap;
import java.util.HashSet;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.Connections;
import bgu.spl.net.application.messages.ErrorMessage;
import bgu.spl.net.application.messages.ServerToClientMessage;
import bgu.spl.net.application.messages.ClientToServerMessage;

public class PM_Message extends ClientToServerMessage {
    private String messageToPost;
    private String targetUser;
    private String dateAndTime;

    private HashSet<String> filteredWords;

    public PM_Message(String targetUser, String messageToPost, String dateAndTime){
        super(6);
        this.messageToPost=messageToPost;
        this.targetUser=targetUser;
        this.dateAndTime=dateAndTime;

        filteredWords=new HashSet<>();
        filteredWords.add("war");
        

    }

    @Override
    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        
        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        UserSession targetUserSession=usernameToUserSession.get(targetUser);

        if(targetUserSession==null || ! targetUserSession.isFollowedByThisUser(currentUserSession.getUsername()))
            return error();
        
        filterMessage();

        NotificationMessage wrappedMessage = new NotificationMessage(6,currentUserSession.getUsername(),messageToPost);
        Byte[] encodedMessage=EncDec.encode(wrappedMessage);

        if(targetUserSession.isLoggedIn())           //FIXME: concurrency: user logouts during sending
            connections.getHandler(targetUserSession.getSessionId()).send(encodedMessage); 
        else{
         targetUserSession.getReceivedMessages().add(encodedMessage);
        } 
        
        return ack();
               
        
    }

    private boolean needsToBeFiltered(String word){
        return filteredWords.contains(word);
    }

    private void filterMessage(){
        
        String[] messageByWords=messageToPost.split(" ");

        for(int i=0;i<messageByWords.length;++i){
            if(needsToBeFiltered(messageByWords[i]))
                messageByWords[i]="<filtered>";
        }

        String filteredMessage="";

        for(int i=0;i<messageByWords.length;++i){
           if(i!=messageByWords.length-1)
                filteredMessage= filteredMessage.concat(messageByWords[i]+" ");
           else
                filteredMessage= filteredMessage.concat(messageByWords[i]);
        }

               
        messageToPost=filteredMessage;

    }
    
}
