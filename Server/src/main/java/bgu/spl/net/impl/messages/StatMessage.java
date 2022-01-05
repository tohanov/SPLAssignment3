package bgu.spl.net.impl.messages;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.messages.LogstatMessage.UserStats;

public class StatMessage extends ClientToServerMessage {

    private ArrayList<String> users;
    
    public StatMessage(ArrayList<String> users) {
        super(8);
        this.users = users;
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();

        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        ArrayList<UserStats> output = new ArrayList<>();

        if (!usernameToUserSession.keySet().containsAll(users)) {   //checks all users exist
            return error();
        }

        for(String username: users){
            
            UserSession userSession = usernameToUserSession.get(username);
            
            if(!userSession.isBlockingOtherUser(currentUserSession.getUsername())){
                output.add(new UserStats(userSession.getAge(), userSession.getNumOfPosts(), userSession.getNumOfFollwers(), userSession.getNumOfFollwing()));
            }

        }

        return ack(output);
    } 


}
