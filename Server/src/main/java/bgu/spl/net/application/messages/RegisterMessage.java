package messages;

import java.util.HashMap;

import bgu.spl.net.application.UserSession;

public class RegisterMessage extends Message {
    
    private String username;
    private String password;
    private String birthday;

    public RegisterMessage(String username, String password, String birthday){
        this.username=username;
        this.password=password;
        this.birthday=birthday;
        
    }

    public Message act(Connections connection, HashMap<String,UserSession> usernameToUserSessionHashMap){

        UserSession currentUserSession=usernameToUserSessionHashMap.putIfAbsent(username, new UserSession(username, password, birthday));
        if(currentUserSession!=null)
            return new ErrorMessage();


        return new AckMessage();         

    }
}