import java.util.ArrayList;
import java.util.HashMap;

import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.ClientToServerMessage;
import bgu.spl.net.application.messages.Connections;
import bgu.spl.net.application.messages.ErrorMessage;
import bgu.spl.net.application.messages.LogstatMessage.UserStats;

public class StatMessage extends ClientToServerMessage {

    private ArrayList<String> users;
    
    public StatMessage(ArrayList<String> users) {
        this.users = users;
    }



    @Override
    public ClientToServerMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        
        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return new ErrorMessage();

        ArrayList<UserStats> output = new ArrayList<>();

        if (!usernameToUserSession.keySet().containsAll(users)) {
            return new ErrorMessage();
        }

        for(String username: users){
            UserSession userSession = usernameToUserSession.get(username);
            
            output.add(new UserStats(userSession.getAge(), userSession.getNumOfPosts(), userSession.getNumOfFollwers(), userSession.getNumOfFollwing()));

        }

        return new StatAckMessage(output);
    } 


}
