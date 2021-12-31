package messages;

import java.util.HashMap;

import main.java.bgu.spl.net.application.UserSession;

public class RegisterMessage extends Message {
    
    private String username;
    private String password;
    private String birthday;

    public RegisterMessage(String username, String password, String birthday){
        this.username=username;
        this.password=password;
        this.birthday=birthday;
        
    }

    public Message act(Connection connection, HashMap<UserSession,Integer> userToIdHashMap){

        Integer id=userToIdHashMap.putIfAbsent(new UserSession(username, password), -1);  // in case two clients try to register the same username at the same time 
        
        if(id!=null)
            return new ErrorMessage();


        return new AckMessage();         

    }
}
