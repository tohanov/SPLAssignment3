package bgu.spl.net.impl.messages;

public class AckMessage extends ServerToClientMessage{

    Object information;

    AckMessage(int messageOpCode){
        super(messageOpCode,10);
        
    }

    AckMessage(int messageOpCode, Object args){
        super(messageOpCode,10);

        information = args;
    }

    public Object getInformation(){
        return information;
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
