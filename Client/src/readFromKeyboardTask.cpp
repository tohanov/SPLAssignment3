#include "readFromKeyboardTask.h"
#include <algorithm>
	
readFromKeyboardTask::readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx)
 : ptr_connectionHandler(_ptr_connectionHandler), mtx(_mtx) {
}

readFromKeyboardTask::~readFromKeyboardTask() {
}


void readFromKeyboardTask::run() { // FIXME : make a normal function to get rid of compilation errors
	string line;
	
	while (1) { // TODO: check if shouldTerminate via the connection handler?
		// const short bufsize = 1024;
		// char buf[bufsize];
		
		// std::cin.getline(buf, bufsize);
		// std::string line(buf);

		getline(cin, line);

		// int len = line.length();
		transform(line.begin(), line.end(), line.begin(), ::tolower);
		cout << "before sendline()" << std::endl;

		// std::lock_guard<std::mutex> lock(mtx);

		if (!ptr_connectionHandler->sendLine(line)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}
		cout << "after sendline()" << std::endl;
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
		// std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

		if(line=="LOGOUT")
			break;
	}
}
