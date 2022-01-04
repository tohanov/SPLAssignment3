package bgu.spl.net.application.messages;

public class ErrorMessage extends ServerToClientMessage {
    
    public ErrorMessage(int messageOpCode){
        super(messageOpCode,11);
        
    }

}
