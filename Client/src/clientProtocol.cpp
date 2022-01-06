//
// Created by USER on 28/12/2021.
//

#include "clientProtocol.h"

template <typename T>
clientProtocol<T>::clientProtocol():should_terminate(false){}
    
template <typename T>
void clientProtocol<T>::process(T message){
    short opCode = getOpCode(message);

    switch(opCode){
        case 9:
            resolveNotification(message);
        case 10:
            resolveAck();
        case 11:
            resolveError()
    }

}

void resolveNotification(NotificationMessage message){
    string output="NOTIFICATION";
    output += " ";
    output += message.getType();    //public or pm
    output += " ";
    output += message.getPostingUser();
    output += " ";
    output += message.getContent();

    cout<<output<<std::endl;
}

void resolveAck(AckMessage message){
    string output="ACK";
    output += " ";

    if(message.getMessageOpCode() == '4'){

    }
    else{

    }

    cout<<output<<std::endl;
}

void resolveError(ErrorMessage message){
    string output="ERROR";
    output += " ";
    output += message.getMessageOpCode();

    cout<<output<<std::endl;
}


template <typename T>
short clientProtocol<T>::getOpCode(T message){
    return;
}




