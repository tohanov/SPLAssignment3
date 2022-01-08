package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.ConnectionHandler;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.ConnectionHandlerImpl;
import bgu.spl.net.impl.UserSession;
import bgu.spl.net.impl.ConnectionsImpl;

public class RegisterMessage extends ClientToServerMessage {
    
    private String username;
    private String password;
    private String birthday;

    public RegisterMessage(String username, String password, String birthday){
        super(1);

		System.out.println(username);
		System.out.println(password);
		System.out.println(birthday);

        this.username=username;
        this.password=password;
        this.birthday=birthday;
        
    }

    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSessionHashMap){

		
        

		ConnectionHandler<Message> handler = ((ConnectionsImpl<Message>)connections).getHandler(currentUserId);

		// if name is registered already or connection logged in as another user
        if(handler.getUserSession() != null) { // if current connection is logged in
		    return error();
		}

		UserSession currentUserSession=usernameToUserSessionHashMap.putIfAbsent(
			username, 
			new UserSession(username, password, birthday)
		);

		if (currentUserSession != null) { // if named user exists already
		    return error();
		}

        return ack();       

    }
}
