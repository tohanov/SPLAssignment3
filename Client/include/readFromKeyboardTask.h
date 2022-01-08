#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>
#include <mutex>
#include "logoutStages.h"


class readFromKeyboardTask{
	private:
		ConnectionHandler *ptr_connectionHandler;
		std::mutex &mtx;
		LogoutStages &logoutStage;
		
	public:
		readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler, std::mutex &_mtx, LogoutStages &logoutStage);

		~readFromKeyboardTask();
		
		void run();
};