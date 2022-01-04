import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import Messages.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.application.UserSession;
import bgu.spl.net.application.messages.ClientToServerMessage;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<ClientToServerMessage> {

    private Connections connections;
    private HashMap<String,UserSession> usernameToUserSession;
    private boolean shouldTerminate;
    
    public BidiMessagingProtocolImpl(ConcurrentHashMap<String,Integer> usernameToUserSession){
        this.usernameToUserSession=usernameToUserSession;
        shouldTerminate=false;

    }


    @Override
    public void start(int connectionId, Connections connections) {
        this.connections=connections;
        connections.addHandler(id, this);
    }

    @Override
    public void process(ClientToServerMessage message) {
        message.act(currentUserSession, connections, usernameToUserSession);
        
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    
}
