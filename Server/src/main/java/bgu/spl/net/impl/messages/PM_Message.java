package bgu.spl.net.impl.messages;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.ConnectionsImpl;


public class PM_Message extends ClientToServerMessage {
    private String messageToPost;
    private String targetUser;
    private String dateAndTime;

	private static class FilteredWordsWrapper {
		private static HashSet<String> filteredWords = new HashSet<String>(
			Arrays.asList(
				new String[] {
					"war",
					"kill",
					"trump",
					"murder",
					"blood",
				}
			).stream().map(String::toLowerCase).collect(Collectors.toSet())
		);
	}

    public PM_Message(String targetUser, String messageToPost, String dateAndTime){
        super(6);
        this.messageToPost=messageToPost;
        this.targetUser=targetUser;
        this.dateAndTime=dateAndTime;
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {
        
        UserSession currentUserSession = ((ConnectionsImpl<Message>)connections).getHandler(currentUserId).getUserSession();

        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        UserSession targetUserSession=usernameToUserSession.get(targetUser);

        if(targetUserSession==null || ! targetUserSession.isFollowedByThisUser(currentUserSession.getUsername()))
            return error();
        
        filterMessage();

        NotificationMessage wrappedMessage = new NotificationMessage(6,currentUserSession.getUsername(), messageToPost + ' ' + dateAndTime);
        
        if(targetUserSession.isLoggedIn())           //FIXME: concurrency: user logouts during sending
			((ConnectionsImpl<Message>)connections).getHandler(targetUserSession.getSessionId()).send(wrappedMessage); 
        else{
         targetUserSession.getReceivedMessages().add(wrappedMessage);
        } 
        
        return ack();
               
        
    }

    private boolean needsToBeFiltered(String word){
        return FilteredWordsWrapper.filteredWords.contains(word.toLowerCase());
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
