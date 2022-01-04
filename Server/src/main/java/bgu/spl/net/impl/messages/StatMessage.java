package bgu.spl.net.impl.messages;
import java.util.ArrayList;
import java.util.HashMap;

import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.messages.ClientToServerMessage;
import bgu.spl.net.impl.messages.Connections;
import bgu.spl.net.impl.messages.ErrorMessage;
import bgu.spl.net.impl.messages.LogstatMessage.UserStats;

public class StatMessage extends ClientToServerMessage {

    private ArrayList<String> users;
    
    public StatMessage(ArrayList<String> users) {
        super(8);
        this.users = users;
    }



    @Override
    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        
        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        ArrayList<UserStats> output = new ArrayList<>();

        if (!usernameToUserSession.keySet().containsAll(users)) {   //checks all users exist
            return error();
        }

        for(String username: users){
            
            UserSession userSession = usernameToUserSession.get(username);
            
            if(!userSession.isBlockingOtherUser(currentUserSession.getUsername()))
                 output.add(new UserStats(userSession.getAge(), userSession.getNumOfPosts(), userSession.getNumOfFollwers(), userSession.getNumOfFollwing()));

        }

        return ack(output);
    } 


}
