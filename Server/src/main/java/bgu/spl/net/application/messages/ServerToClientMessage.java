package bgu.spl.net.application.messages;

public abstract class ServerToClientMessage {

   
    private short MessageOpCode;
    private short opCode;

    public ServerToClientMessage(int messageOpCode, int opCode) {
        this.MessageOpCode = (short)messageOpCode;
        this.opCode = (short)opCode;
    }

    

    public short getMessageOpCode() {
        return MessageOpCode;
    }

        
}
