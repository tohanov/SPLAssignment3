package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.ConnectionHandlerImpl;
import bgu.spl.net.impl.UserSession;

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

        UserSession currentUserSession=usernameToUserSessionHashMap.putIfAbsent(username, new UserSession(username, password, birthday));
		ConnectionHandlerImpl<Message> handler = connections.getHandler(currentUserId);

		// if name is registered already or connection logged in as another user
        if(currentUserSession!=null || handler.getUserSession() != null)
            return error();


        return ack();       

    }
}
