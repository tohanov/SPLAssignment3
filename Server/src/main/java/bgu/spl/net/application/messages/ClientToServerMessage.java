package bgu.spl.net.application.messages;

import java.util.HashMap;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.application.UserSession;

public abstract class ClientToServerMessage {

    final protected short opCode;

    public ClientToServerMessage(int opCode) {
        this.opCode = (short)opCode;
    }

    public ServerToClientMessage act(UserSession currentUserSession, Connections connections, HashMap<String,UserSession> usernameToUserSession); 
    
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
