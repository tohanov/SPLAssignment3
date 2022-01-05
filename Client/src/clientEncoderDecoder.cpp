#include "clientEncoderDecoder.h"
#include <string>
#include <sstream>
#include <unordered_map>

enum OpCode {
    REGISTER = 1,
    LOGIN,
    LOGOUT,
    FOLLOW_UNFOLLOW,
    POST,
    PM,
    LOGSTAT,
    STAT,
    NOTIFICATION,
    ACK,
    ERROR,
    BLOCK
};



bool createdHashMap = false;

char* clientEncoderDecoder<string>::encode(string message){
    static unordered_map<string, int> commandToOpCode;
    if (!createdHashMap) {
        commandToOpCode["register"] = OpCode::REGISTER;
        commandToOpCode["login"] = OpCode::LOGIN;
        commandToOpCode["logout"] = OpCode::LOGOUT;
        commandToOpCode["follow"] = OpCode::FOLLOW_UNFOLLOW;
        commandToOpCode["unfollow"] = OpCode::FOLLOW_UNFOLLOW;
        commandToOpCode["post"] = OpCode::POST;
        commandToOpCode["pm"] = OpCode::PM;
        commandToOpCode["logstat"] = OpCode::LOGSTAT;
        commandToOpCode["stat"] = OpCode::STAT;
        commandToOpCode["block"] = OpCode::BLOCK;
    
        createdHashMap = true;
    }
    
   

    string command;
    istringstream messageStream(message);

    messageStream >> command;

    switch (commandToOpCode[command])
    {
     case REGISTER:
        return encodeRegisterm(messageStream);
    case REGISTER:
        encodeLogin();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    case REGISTER:
        encodeRegister();
     break;
    }
}

char* encodeRegister(istringstream messageStream){
    string username,password,birthday;

    messageStream >> username;
    messageStream >> password;
    messageStream >> birthday;

    char *encodedMessage = new char[5 + username.length() + password.length() + birthday.length()];

    ostringstream output(encodedMessage);
    shortToBytes(OpCode::REGISTER , encodedMessage);
    output << username << 0 << password << 0 << birthday << 0 << ';';

    return encodedMessage;
}

void shortToBytes(short num, char* bytesArr)

{

    bytesArr[0] = ((num >> 8) & 0xFF);

    bytesArr[1] = (num & 0xFF);

}