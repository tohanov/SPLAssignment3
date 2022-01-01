package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.application.UserSession;

public abstract class Message {

    public abstract Message act(Integer id, Connections connections, HashMap<String,UserSession> usernameToUserSession); 
    
}
