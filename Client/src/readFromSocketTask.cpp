#include "readFromSocketTask.h"

readFromSocketTask::readFromSocketTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx)
 : ptr_connectionHandler(_ptr_connectionHandler), mtx(_mtx) {
}

readFromSocketTask::~readFromSocketTask() {
}

void readFromSocketTask::run() { // FIXME : make a normal function to get rid of compilation errors
// TODO : Loop
	
	std::string answer;

	while (1) {
		cout << "[socket task] before read from server" << std::endl;


		// std::lock_guard<std::mutex> lock(mtx);

		if (!ptr_connectionHandler->getLine(answer)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}

		cout << "[socket task] read from server: " << answer << std::endl;

		int len=answer.length();

		// answer.resize(len-1);
		std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
		if (answer == "ACK 3") {   //TODO: change format if needed
			std::cout << "Exiting...\n" << std::endl;
			break;
		}
	}

	//answer is valid

// return 0;
}

