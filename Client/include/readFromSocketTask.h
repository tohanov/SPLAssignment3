#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>
#include <mutex>

class readFromSocketTask {
	private:
		ConnectionHandler *ptr_connectionHandler;
		std::mutex &mtx;

	public:
		readFromSocketTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx);

		~readFromSocketTask();

		void run();
};
