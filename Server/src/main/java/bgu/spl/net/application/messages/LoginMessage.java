package main.java.bgu.spl.net.application.messages;

import main.java.bgu.spl.net.application.messages.Message;
import java.sql.Connection;
import java.util.HashMap;

import main.java.bgu.spl.net.application.UserInfo;

public class LoginMessage extends Message {

    private String username;
    private String password;
   
    public LoginMessage(String username, String password, String birthday){
        this.username=username;
        this.password=password;
              
    }

    @Override
    public Message act(Connection connection, HashMap<UserInfo, Integer> userToIdHashMap) {
        Integer connectionId=userToIdHashMap.get(new UserInfo(username, password)); 

        if(connectionId==null || connectionId==-1)
            return new ErrorMessage();

        if(password=)   //TODO: check password

        //TODO: update connectionId in a concurrent way
    }

    
    
}
