#include <string>
#include <iostream>

#include "clientProtocol.h"
#include "clientConnectionHandler.h"


bool isResponceMessage(string &message);


template<> clientProtocol<string>::clientProtocol(ConnectionHandler *handler)
	: should_terminate(false), ptr_handler(handler) {
}


template<> void clientProtocol<string>::process(string message) {
	if (isResponceMessage(message)) {
		if (message == "ACK 3") {
			ptr_handler->setLogoutStage(ClientProgramStages::LogoutStages::LOGGED_OUT);
		}

		cout << "<< " << message << endl; //FIXME mutex
		ptr_handler->setCommandStage(ClientProgramStages::MessageStages::WAITING_TO_SEND);
	}
	else { // server initiated communication, hence a notification message
		if (ptr_handler->getCommandStage() == ClientProgramStages::MessageStages::WAITING_FOR_RESPONSE) {
			// can print normally cause there isn't an input prompt currently on the screen

			cout << "[*] " << message << endl;
		}
		else {
			cout << "\n[*] " << message << "\n>> " << flush;
		}
	}
}


template<> bool clientProtocol<string>::shouldTerminate() {
	return should_terminate;
}


bool isResponceMessage(string &message) {
	return message.find("ACK") == 0 || message.find("ERROR") == 0;
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