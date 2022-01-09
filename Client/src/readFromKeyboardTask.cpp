#include "readFromKeyboardTask.h"
#include <algorithm>
#include <iostream>
#include <chrono>
#include <thread>


readFromKeyboardTask::readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler/* , std::mutex &_mtx */)
 : ptr_connectionHandler(_ptr_connectionHandler)/* , mtx(_mtx) */ {
}


readFromKeyboardTask::~readFromKeyboardTask() {
}

void readFromKeyboardTask::run() { // FIXME : make a normal function to get rid of compilation errors
	string line;

	// cout << ">> "; // FIXME mutex

	while (ptr_connectionHandler->getLogoutStage() != ClientProgramStages::LogoutStages::LOGGED_OUT) { // TODO: check if shouldTerminate via the connection handler?
		// const short bufsize = 1024;
		// char buf[bufsize];
		
		// std::cin.getline(buf, bufsize);
		// std::string line(buf);
		cout << ">> ";
		getline(cin, line);

		// int len = line.length();
		// cout << "before sendline()" << std::endl;

		// std::lock_guard<std::mutex> lock(mtx);

		if (!ptr_connectionHandler->sendLine(line)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}
		// cout << "after sendline()" << std::endl;
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
		// std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

		// if (logoutStage == LogoutStages::WAITING_FOR_ACK) { 
		// 	cout << "[*] waiting for ack for logout" << endl;
		// }

		while (ptr_connectionHandler->getCommandStage() == ClientProgramStages::MessageStages::WAITING_FOR_RESPONSE) {
			// wait for 0.5 secs
			// cout << "waiting" << endl; //FIXME remove
			std::this_thread::sleep_for(std::chrono::milliseconds(10));
		}
	}
	
	// std::cout << "out of loop in keyboardtask" << std::endl;
}
