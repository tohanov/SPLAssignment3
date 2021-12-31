import java.util.concurrent.ConcurrentHashMap;

import Messages.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private Connections connections;
    private ConcurrentHashMap<String,Integer> userToIdHashmap;
    private boolean shouldTerminate;
    
    public BidiMessagingProtocolImpl(Connections connections, ConcurrentHashMap<String,Integer> userToIdHashmap){
        this.connections=connections;
        this.userToIdHashmap=userToIdHashmap;
        shouldTerminate=false;

    }


    @Override
    public void start(int connectionId, Connections connections) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void process(Message message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    
}
