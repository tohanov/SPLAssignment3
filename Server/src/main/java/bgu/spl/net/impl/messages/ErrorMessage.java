package bgu.spl.net.impl.messages;

public class ErrorMessage extends ServerToClientMessage {
    
    public ErrorMessage(int messageOpCode){
        super(messageOpCode,11);
        
    }

}
