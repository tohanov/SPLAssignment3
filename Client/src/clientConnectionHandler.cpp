#include "clientConnectionHandler.h"
#include "clientEncoderDecoder.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : 
host_(host), port_(port), io_service_(), socket_(io_service_), encDec(), protocol()
{
}

// ConnectionHandler::ConnectionHandler(ConnectionHandler &other) : socket_(other.socket_) {
// }

ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;

		exit(-1); // FIXME : remove
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
	cout << "[*] inside sendBytes()" << endl;
    int tmp = 0;
    
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;

		exit(-1); // FIXME : remove
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    // return getFrameAscii(line, '\n');
    string decoded="";
    char ch;
    try {
		do{
			getBytes(&ch, 1);
            decoded = encDec.decodeNextByte(ch);
        } while (decoded == "");
    }
	catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;

		exit(-1); // FIXME : remove
        return false;
    }
    protocol.process(decoded);
	// FIXME protocol call
	line = decoded;
    return true;
}

bool ConnectionHandler::sendLine(std::string& line) {
    char *ptr_Bytes = encDec.encode(line);
	char *ptr_semicolon = ptr_Bytes;

	while (*ptr_semicolon != ';') ++ptr_semicolon;

    int len = ptr_semicolon - ptr_Bytes + 1; // strchr(ptr_Bytes,';') - ptr_Bytes + 1;

	cout << "[*] inside sendLine(), len=" << len << endl;

    bool connectionClosed = sendBytes(ptr_Bytes, len);

    delete ptr_Bytes;
    return connectionClosed;
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;

		exit(-1); // FIXME : remove
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}


// bool ConnectionHandler::shouldTerminate() {
// 	return should_terminate;
// }