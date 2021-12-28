//
// Created by USER on 28/12/2021.
//

class readFromKeyboardTask{

private:
    ConnectionHandler connectionHandler;
public:

    readFromKeyboardTask(ConnectionHandler connectionHandler){
        this->connectionHandler=connectionHandler;
    }

    void operator()() {
        while (1) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            int len = line.length();
            if (!connectionHandler.sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
            std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

            if(line=="LOGOUT")
                break;




        }

    }

};