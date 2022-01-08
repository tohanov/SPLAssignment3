#include <clientConnectionHandler.h>  
#include <thread>
#include "readFromKeyboardTask.h"
#include "readFromSocketTask.h"
#include <iostream>
#include "logoutStages.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

	LogoutStages logoutStage = LogoutStages::NOT_LOGGED_OUT;

    ConnectionHandler connectionHandler(host, port);

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    
	std::mutex mtx; // FIXME needed when printing notification and stuff

    readFromKeyboardTask keyboardTask(&connectionHandler, mtx, logoutStage);
    readFromSocketTask socketTask(&connectionHandler, mtx, logoutStage);

    // std::thread th1(&Task::run, &task1);
    // std::thread th2(&Task::run, &task2);

    std::thread socketThread(&readFromSocketTask::run, &socketTask);
    std::thread keyboardThread(&readFromKeyboardTask::run, &keyboardTask);
    
    socketThread.join();
	// FIXME sync using mutex with notifications
	// keyboardThread.terminate();
    keyboardThread.join();
	
    return 0;
}
