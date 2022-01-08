#include <string>
#include <iostream>

#include "clientProtocol.h"


template<typename T> clientProtocol<T>::clientProtocol() : should_terminate(false) {
}

template<> clientProtocol<string>::clientProtocol() : should_terminate(false) {
}


template<> void clientProtocol<string>::process(string message) {
    cout << message << std::endl; //FIXME mutex
}


template<typename T> bool clientProtocol<T>::shouldTerminate() {
	return should_terminate;
}


// void resolveNotification(NotificationMessage message) {
//     string output="NOTIFICATION";
//     output += " ";
//     output += message.getType();    //public or pm
//     output += " ";
//     output += message.getPostingUser();
//     output += " ";
//     output += message.getContent();

//     cout<<output<<std::endl;
// }


// void resolveAck(AckMessage message) {
//     string output="ACK";
//     output += " ";

//     if(message.getMessageOpCode() == '4') {

//     }
//     else{

//     }

//     cout<<output<<std::endl;
// }


// void resolveError(ErrorMessage message) {
//     string output="ERROR";
//     output += " ";
//     output += message.getMessageOpCode();

//     cout<<output<<std::endl;
// }


// short clientProtocol<string>::getOpCode(string message) {
//     return;
// }