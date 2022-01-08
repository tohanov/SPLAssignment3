package bgu.spl.net.impl.messages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.messages.LogstatMessage.UserStats;
import bgu.spl.net.impl.ConnectionsImpl;

public class StatMessage extends ClientToServerMessage {

    private String[] users;
    
    public StatMessage(String[] users) {
        super(8);
        this.users = users;
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {

        UserSession currentUserSession = ((ConnectionsImpl<Message>)connections).getHandler(currentUserId).getUserSession();

        if(
			currentUserSession==null // if current connection not logged in
			|| !usernameToUserSession.keySet().containsAll(Arrays.asList(users)) // if some of the requested users doesn't exist
		) {   //checks all users exist
            return error();
        }

        ArrayList<UserStats> output = new ArrayList<>();

        for(String username: users){
            UserSession userSession = usernameToUserSession.get(username);
            
            if(userSession.isBlockingOtherUser(currentUserSession.getUsername())){
                return error();
            }

			output.add(new UserStats(userSession.getAge(), userSession.getNumOfPosts(), userSession.getNumOfFollwers(), userSession.getNumOfFollwing()));
        }

        return ack(output);
    } 


}
