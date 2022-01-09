#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>
#include <mutex>


class readFromKeyboardTask{
	private:
		ConnectionHandler *ptr_connectionHandler;
		// std::mutex &mtx;
		
	public:
		readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler/* , std::mutex &_mtx */);

		~readFromKeyboardTask();
		
		void run();
};