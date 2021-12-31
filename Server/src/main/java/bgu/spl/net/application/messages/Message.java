
package main.java.bgu.spl.net.application.messages;

import java.sql.Connection;
import java.util.HashMap;

public abstract class Message {

    public abstract Message act(Connection connection, HashMap<UserSession,Integer> userToIdHashMap); 
    
}
