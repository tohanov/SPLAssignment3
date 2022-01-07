package bgu.spl.net.impl;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.messages.AckMessage;
import bgu.spl.net.impl.messages.ClientToServerMessage;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.messages.ServerToClientMessage;
import bgu.spl.net.bidi.BidiMessagingProtocol;
import bgu.spl.net.bidi.ConnectionHandler;


public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    private ConnectionsImpl<Message> connections;
    private ConcurrentHashMap<String,UserSession> usernameToUserSession;
    private boolean shouldTerminate;
    private ConnectionHandlerImpl<Message> handler;
    private int id;


    public BidiMessagingProtocolImpl(ConcurrentHashMap<String,UserSession> usernameToUserSession){
        this.usernameToUserSession=usernameToUserSession;
        shouldTerminate=false;
    }


    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections=(ConnectionsImpl<Message>) connections;
		System.out.println(connectionId + " " + handler);
        ((ConnectionsImpl<Message>) connections).addIdToConnections(connectionId, handler);
        id = connectionId;
    }

    @Override
    public void process(Message message) {

        ServerToClientMessage response=((ClientToServerMessage) message).act(id, connections, usernameToUserSession);
        
        shouldTerminate = response.getMessageOpCode()==3 && response instanceof AckMessage;

        // if response not null - always
            // encode
            // send as bytes
        
        handler.send(response);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setHandler(ConnectionHandler<Message> handler) {
		System.out.println("[*] inside setHandler, handler=" + handler);
        this.handler=(ConnectionHandlerImpl<Message>) handler;
        
    }
    
}
