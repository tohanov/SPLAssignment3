package bgu.spl.net.impl.messages;

public class AckMessage extends ServerToClientMessage{

    Object information; //TODO remove
	ClientToServerMessage respondingToMessage;

	AckMessage(ClientToServerMessage message){ //FIXME get message object
        super(message.opCode, 10);
        
    }


    AckMessage(int messageOpCode){ //FIXME get message object
        super(messageOpCode,10);
        
    }

    AckMessage(int messageOpCode, Object args){
        super(messageOpCode,10);

        information = args;
    }

    public Object getInformation(){
        return information;
    }

	public ClientToServerMessage getRespondingToMessage() {
		return respondingToMessage;
	}


    // AckMessage(int messageOpCode, ArrayList<UserStats> args){
    //     super(messageOpCode);

    // }


    /**
     * Follow- username
     * Logstat- arraylist of userStats
     * Stats- arraylist of userStats
     *  
     */










}
