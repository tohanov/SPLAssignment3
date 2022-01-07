#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>

class readFromSocketTask {
	private:
		ConnectionHandler *ptr_connectionHandler;

	public:
		readFromSocketTask(ConnectionHandler *_ptr_connectionHandler);

		~readFromSocketTask();

		void run();
};
