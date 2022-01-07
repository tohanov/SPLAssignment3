#include "clientConnectionHandler.h"
#include <thread>
#include <iostream>


class readFromKeyboardTask{
	private:
		ConnectionHandler *ptr_connectionHandler;
		
	public:
		readFromKeyboardTask(ConnectionHandler *_ptr_connectionHandler);

		~readFromKeyboardTask();
		
		void run();
};