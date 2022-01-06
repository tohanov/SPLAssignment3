//
// Created by USER on 28/12/2021.
//
#include "clientConnectionHandler.h"

class readFromKeyboardTask{

private:
    ConnectionHandler *ptr_connectionHandler;
public:

    readFromKeyboardTask(ConnectionHandler *_connectionHandler) {
        ptr_connectionHandler = _connectionHandler;
    }
    

    void operator()() {
        string line;
        
        while (1) { // TODO: check if shouldTerminate via the connection handler?

            // const short bufsize = 1024;
            // char buf[bufsize];
            
            // std::cin.getline(buf, bufsize);
            // std::string line(buf);

            getline(cin, line);

            // int len = line.length();

            if (!ptr_connectionHandler->sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
            // std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

            if(line=="LOGOUT")
                break;




        }

    }

};