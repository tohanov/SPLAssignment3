#include <clientConnectionHandler.h>  
#include <thread>
#include "readFromKeyboardTask.h"
#include "readFromSocketTask.h"
#include <iostream>

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

	

    ConnectionHandler connectionHandler(host, port);

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    
	std::mutex mtx;

    readFromKeyboardTask task1(&connectionHandler, mtx);
    readFromSocketTask task2(&connectionHandler, mtx);

    // std::thread th1(&Task::run, &task1);
    // std::thread th2(&Task::run, &task2);

    std::thread th2(&readFromSocketTask::run, &task2);
    std::thread th1(&readFromKeyboardTask::run, &task1);
    
    th2.join();
    th1.join();
	
    return 0;
}
