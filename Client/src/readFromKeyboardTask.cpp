#include "readFromKeyboardTask.h"
	
		readFromKeyboardTask::readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler) : ptr_connectionHandler(_ptr_connectionHandler) {
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

				if (!ptr_connectionHandler->sendLine(line)) {
					std::cout << "Disconnected. Exiting...\n" << std::endl;
					break;
				}
				// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
				// std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

				if(line=="LOGOUT")
					break;
			}
		}
