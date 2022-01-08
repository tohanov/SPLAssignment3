#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>
#include <mutex>
#include "logoutStages.h"


class readFromSocketTask {
	private:
		ConnectionHandler *ptr_connectionHandler;
		std::mutex &mtx;
		LogoutStages &logoutStage;

	public:
		readFromSocketTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx, LogoutStages &logoutStage);

		~readFromSocketTask();

		void run();
};
