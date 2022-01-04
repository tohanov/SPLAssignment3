package bgu.spl.net.impl.messages;

import java.util.HashMap;

import bgu.spl.net.impl.UserSession;

public class RegisterMessage extends ClientToServerMessage {
    
    private String username;
    private String password;
    private String birthday;

    public RegisterMessage(String username, String password, String birthday){
        super(1);
        this.username=username;
        this.password=password;
        this.birthday=birthday;
        
    }

    public ServerToClientMessage act(Connections connection, HashMap<String,UserSession> usernameToUserSessionHashMap){

        UserSession currentUserSession=usernameToUserSessionHashMap.putIfAbsent(username, new UserSession(username, password, birthday));
        if(currentUserSession!=null)
            return error();


        return ack();       

    }
}
