package bgu.spl.net.application.messages;

import java.util.ArrayList;

import bgu.spl.net.application.messages.LogstatMessage.UserStats;

public class AckMessage extends ServerToClientMessage{

    Object information;

    AckMessage(int messageOpCode){
        super(messageOpCode,10);
        
    }

    AckMessage(int messageOpCode, Object args){
        super(messageOpCode,10);

        information = args;
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
