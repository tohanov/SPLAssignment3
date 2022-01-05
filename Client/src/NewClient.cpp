#include <stdlib.h>
#include <connectionHandler.h>  
#include <thread>
#include "readFromKeyboardTask.cpp"
#include "readFromSocketTask.cpp"
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

    newConnectionHandler connectionHandler(host, port);

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    
    readFromKeyBoardTask task1(connectionHandler);
    readFromSocketTask task2(connectionHandler);

    std::thread th1(std::ref(task1));
    std::thread th2(std::ref(task2));

    th1.join();
    th2.join();



    return 0;
}
