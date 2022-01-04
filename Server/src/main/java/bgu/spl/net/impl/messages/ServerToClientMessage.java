package bgu.spl.net.impl.messages;

public abstract class ServerToClientMessage extends Message {

   
    private short MessageOpCode;

    public ServerToClientMessage(int messageOpCode, int opCode) {
        super(opCode);
        this.MessageOpCode = (short)messageOpCode;
    }

    

    public short getOpCode() {
        return opCode;
    }

    

    public short getMessageOpCode() {
        return MessageOpCode;
    }

        
}
