#include "clientEncoderDecoder.h"
#include <string>
#include <algorithm>
#include <string.h>
#include <sstream>
#include <unordered_map>
#include <vector>

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
    vector<char> outputVector;
    istringstream messageStream(message);
    messageStream >> command;

    char encodedOpCode[] = {0,0};

    shortToBytes(commandToOpCode[command], encodedOpCode);
    
    addBytesToVector(outputVector, encodedOpCode, 2);



    switch (commandToOpCode[command])
    {
     case REGISTER:
        encodeRegister(messageStream, outputVector);
    case LOGIN:
        encodeLogin(messageStream, outputVector);
     
    // case LOGOUT:
    //     encodeLogout();
     
    case FOLLOW_UNFOLLOW:
        encodeFollowUnfollow(messageStream, outputVector);
     
    case POST:
        encodePost(messageStream, outputVector);    // "POST " is 5 chars
     
    case PM:
        encodePM(messageStream, outputVector);    //date and time??
     
    // case LOGSTAT:
    //     encodeLogstat(messageStream);
     
    case STAT:
        encodeStat(messageStream, outputVector);
     
    case BLOCK:
        encodeBlock(messageStream, outputVector);
    
    default:
            
    }

    outputVector.push_back(';');

    char* result = new char[outputVector.size()];

    memcpy(result, outputVector.data(), outputVector.size());

    return result;

}
//-------------------------------
//encoding auxiliary functions
// when to add ';'?

void encodeRegister(istringstream& messageStream, vector<char> &outputVector){
    string username,password,birthday;

    messageStream >> username;
    messageStream >> password;
    messageStream >> birthday;
       
    addBytesToVector(outputVector, username.c_str(), username.length() + 1);
    addBytesToVector(outputVector, password.c_str(), password.length() + 1);
    addBytesToVector(outputVector, birthday.c_str(), birthday.length() + 1);

}

void encodeLogin(istringstream& messageStream, vector<char> &outputVector){
    string username,password;
    int captcha;
 
    messageStream >> username;
    messageStream >> password;
    messageStream >> captcha;
   
    addBytesToVector(outputVector, username.c_str(), username.length() + 1);
    addBytesToVector(outputVector, password.c_str(), password.length() + 1);
    
    outputVector.push_back(captcha);

}



void encodeFollowUnfollow(istringstream& messageStream, vector<char> &outputVector){
    int FollowOrUnfollowByte;
    string username;

    messageStream >> FollowOrUnfollowByte;
    messageStream >> username;

    outputVector.push_back(FollowOrUnfollowByte);
    addBytesToVector(outputVector, username.c_str(), username.length() + 1);


}

void encodePost(istringstream& messageStream, vector<char> &outputVector){
    string temp;
    getline(messageStream, temp);

    addBytesToVector(outputVector, temp.c_str(), temp.length() + 1);    

}

void encodePM(istringstream& messageStream, vector<char> &outputVector){

    string username, content;

    messageStream >> username;
    getline(messageStream, content);
    
    addBytesToVector(outputVector, username.c_str(), username.length() + 1); 
    addBytesToVector(outputVector, content.c_str(), content.length() + 1); 

    //date and time
    char buffer[256];
    getTime(buffer, 256);

    addBytesToVector(outputVector, buffer, 17);    

}


void encodeStat(istringstream& messageStream, vector<char> &outputVector){
  string temp;
  getline(messageStream, temp);

  std::replace( temp.begin(), temp.end(), ' ', '|'); // replace all ' ' to '|'

  addBytesToVector(outputVector, temp.c_str(), temp.length() + 1);
 
}

void encodeBlock(istringstream& messageStream, vector<char> &outputVector){

    string username;

    messageStream >> username;
    addBytesToVector(outputVector, username.c_str(), username.length() + 1);

}

//-------------------------------

void shortToBytes(short num, char* bytesArr)

{

    bytesArr[0] = ((num >> 8) & 0xFF);

    bytesArr[1] = (num & 0xFF);

}

void addBytesToVector(vector<char> &outputVector, const char* bytes, int len) {
    for (int i = 0; i < len; ++i){
        outputVector.push_back(bytes[i]);
    }
}

void getTime(char *buffer, int size){
    time_t rawtime;

    time(&rawtime );
    struct tm *info = localtime( &rawtime );
    const time_t now = time(0);

    strftime(buffer, size, "%d-%m-%Y %H:%M", info);
}
