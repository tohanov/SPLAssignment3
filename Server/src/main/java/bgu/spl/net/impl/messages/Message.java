package bgu.spl.net.impl.messages;

public abstract class Message {

    protected short opCode;
    
    public Message(int opCode) {
        this.opCode = (short)opCode;
    }

    public short getOpCode() {
        return opCode;
    }
    
}
