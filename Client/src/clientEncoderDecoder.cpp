#include <string>
#include <algorithm>
#include <string.h>
#include <sstream>
#include <unordered_map>
#include <vector>
#include <iostream>

#include "clientEncoderDecoder.h"

// region Encoder functions
void addBytesToVector(vector<char> &outputVector, const char* bytes, int len);
void getTime(char *buffer, int size);
void shortToBytes(short num, char* bytesArr);

void encodeRegister(istringstream& messageStream, vector<char> &outputVector);
void encodeLogin(istringstream& messageStream, vector<char> &outputVector);
void encodeFollowUnfollow(istringstream& messageStream, vector<char> &outputVector);
void encodePost(istringstream& messageStream, vector<char> &outputVector);
void encodePM(istringstream& messageStream, vector<char> &outputVector);
void encodeStat(istringstream& messageStream, vector<char> &outputVector);
void encodeBlock(istringstream& messageStream, vector<char> &outputVector);
// endregion Encoder functions


// region Decoder functions
string decodeNotification();
string decodeError();
void parseStatLogStat(string &output);
void parseFollowUnfollow(string &output);
string decodeAck();
string returnCompleteMessage();
short bytesToShort(char* bytesArr);
// endregion Decoder functions



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

unordered_map<string, int> commandToOpCode;
vector<char> bytes;

template<> clientEncoderDecoder<string>::clientEncoderDecoder() {
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
}

template<> string clientEncoderDecoder<string>::decodeNextByte(char byte) { //TODO: change to &string
    // std::cout << "[*] inside decodenextbyte" << std::endl;

	if (byte != ';') {
		bytes.push_back(byte);
	
		return "";
	}
	else {
		string output = returnCompleteMessage();

		bytes.clear();

		return output;
	}
}


template<> char* clientEncoderDecoder<string>::encode(string message){
	// std::cout << "[*] encode()" << std::endl;
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
			break;
		case LOGIN:
			encodeLogin(messageStream, outputVector);
			break;

		// case LOGOUT:
		//     encodeLogout();

		case FOLLOW_UNFOLLOW:
			encodeFollowUnfollow(messageStream, outputVector);
			break;

		case POST:
			encodePost(messageStream, outputVector);    // "POST " is 5 chars
			break;

		case PM:
			encodePM(messageStream, outputVector);    //date and time??
			break;

		// case LOGSTAT:
		//     encodeLogstat(messageStream);

		case STAT:
			encodeStat(messageStream, outputVector);
			break;

		case BLOCK:
			encodeBlock(messageStream, outputVector);
			break;

		default:;
    }

    outputVector.push_back(';');

    char* result = new char[outputVector.size()];

    memcpy(result, outputVector.data(), outputVector.size());

    return result;
}


string returnCompleteMessage() {
	// std::cout << "[*] returnCompleteMessage" << std::endl;
	short opCode = bytesToShort(bytes.data());

	switch(opCode) {
		case NOTIFICATION:
			return decodeNotification();
		case ACK:
			return decodeAck();
		case ERROR:
			return decodeError();
	}
}


string decodeAck() {
	// std::cout << "[*] decodeAck" << std::endl;
	string message = "ACK ";
	short messageOpCode = bytesToShort(bytes.data() + 2); // opcode of message to which the ack is responding
	
	message += to_string(messageOpCode);

	switch(messageOpCode) {
		case FOLLOW_UNFOLLOW:
			parseFollowUnfollow(message);
		break;
		case STAT: case LOGSTAT:
			parseStatLogStat(message);
		break;
	}

	return message;
}


void parseFollowUnfollow(string &output) {
	// 2 2 1 name
	output += " " + to_string(bytes[4]) + " "; // follow/unfollow 1 byte
	output += (bytes.data() + 5); // username
}


void parseStatLogStat(string &output) {
	output += " ";
	for (int index = 4; bytes[index] != ';'; index += 8) {
		output += 
		    to_string(bytesToShort(bytes.data() + index)) + ' ' +
			to_string(bytesToShort(bytes.data() + index + 2)) + ' ' +
			to_string(bytesToShort(bytes.data() + index + 4)) + ' ' +
			to_string(bytesToShort(bytes.data() + index + 6)) + '\n';
	}
}


string decodeError() {
	string message = "ERROR ";
	short messageOpCode = bytesToShort(bytes.data() + 2);
	
	message += to_string(messageOpCode);

	return message;
}


string decodeNotification() {
	string message = "NOTIFICATION ";
	int index;

	if (bytes[2] == 1) { // Public post
		message += "Public ";
	}
	else { // PM
		message += "PM ";
	}

	int tempLen = message.length();
	index = 3;

	message += bytes.data() + index; // posting user name
	message += " ";
	index += message.length() - tempLen + 1;

	message += bytes.data() + index;

	return message;
}


short bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);

    result += (short)(bytesArr[1] & 0xff);

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
	// cout << "[*] inside encodeLogin()" << endl;
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
	// cout << "[*] inside encodePM()" << endl;
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

  std::replace( temp.begin(), temp.end(), ' ', '|'); // replace all ' ' with '|'

  addBytesToVector(outputVector, temp.c_str(), temp.length() + 1);
}


void encodeBlock(istringstream& messageStream, vector<char> &outputVector){
    string username;

    messageStream >> username;
    addBytesToVector(outputVector, username.c_str(), username.length() + 1);
}


//-------------------------------
void shortToBytes(short num, char* bytesArr) {
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

    strftime(buffer, size, "%d-%m-%Y %H:%M", info);
}