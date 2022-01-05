package bgu.spl.net.impl.messages;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public abstract class ClientToServerMessage extends Message {

    public ClientToServerMessage(int opCode) {
        super(opCode);
    }

    public abstract ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String,UserSession> usernameToUserSession); 
    
    protected ServerToClientMessage ack() {
        return new AckMessage(opCode);
    }

    protected ServerToClientMessage ack(Object args) {
        return new AckMessage(opCode,args);
    }

    protected ServerToClientMessage error() {
        return error();
    }
}
