#include "clientConnectionHandler.h"


class readFromSocketTask {
	private:
		ConnectionHandler *ptr_connectionHandler;

	public:
		readFromSocketTask(ConnectionHandler *_ptr_connectionHandler) : ptr_connectionHandler(_ptr_connectionHandler) {
		}

		virtual ~readFromSocketTask() {
		}

		void operator()() { // FIXME : make a normal function to get rid of compilation errors
			// We can use one of three options to read data from the server:
			// 1. Read a fixed number of characters
			// 2. Read a line (up to the newline character using the getline() buffered reader
			// 3. Read up to the null character
			std::string answer;
			// Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
			// We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
			if (!ptr_connectionHandler->getLine(answer)) {
				std::cout << "Disconnected. Exiting...\n" << std::endl;
				// break;
			}

			int len=answer.length();
			// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
			// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
			answer.resize(len-1);
			std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
			if (answer == "LOGOUT") {   //TODO: change format if needed
				std::cout << "Exiting...\n" << std::endl;
				// break;
			}

			//answer is valid

		// return 0;
		}
};
