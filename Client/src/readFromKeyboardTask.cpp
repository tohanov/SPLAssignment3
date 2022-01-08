#include "readFromKeyboardTask.h"
#include <algorithm>
#include <iostream>
#include <chrono>
#include <thread>
#include "logoutStages.h"


readFromKeyboardTask::readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx, LogoutStages &_logoutStage)
 : ptr_connectionHandler(_ptr_connectionHandler), mtx(_mtx), logoutStage(_logoutStage) {
}


readFromKeyboardTask::~readFromKeyboardTask() {
}

void readFromKeyboardTask::run() { // FIXME : make a normal function to get rid of compilation errors
	string line;

	while (logoutStage != LogoutStages::LOGGED_OUT) { // TODO: check if shouldTerminate via the connection handler?
		// const short bufsize = 1024;
		// char buf[bufsize];
		
		// std::cin.getline(buf, bufsize);
		// std::string line(buf);

		cout << "> "; // FIXME mutex
		getline(cin, line);

		// int len = line.length();
		// cout << "before sendline()" << std::endl;

		// std::lock_guard<std::mutex> lock(mtx);

		if(line=="logout")
			logoutStage = LogoutStages::WAITING_FOR_ACK;

		if (!ptr_connectionHandler->sendLine(line)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}
		// cout << "after sendline()" << std::endl;
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
		// std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

		while (logoutStage == LogoutStages::WAITING_FOR_ACK) {
			cout << "[remove me] waiting for ack for logout" << endl;
			// wait for 3 secs
			std::this_thread::sleep_for(std::chrono::milliseconds(500));
		}

	}
}
