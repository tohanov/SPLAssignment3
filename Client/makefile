CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -O3
LDFLAGS:=-lboost_system -lpthread

all: client
	g++ -o bin/echoExample bin/connectionHandler.o bin/echoClient.o $(LDFLAGS) 

# EchoClient: bin/connectionHandler.o bin/echoClient.o

client: compile link

compile: src/BGSclient.cpp src/clientConnectionHandler.cpp src/readFromKeyboardTask.cpp src/readFromSocketTask.cpp src/clientProtocol.cpp src/clientEncoderDecoder.cpp 
	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp
	g++ $(CFLAGS) -o bin/clientConnectionHandler.o src/clientConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/readFromKeyboardTask.o src/readFromKeyboardTask.cpp
	g++ $(CFLAGS) -o bin/readFromSocketTask.o src/readFromSocketTask.cpp
	g++ $(CFLAGS) -o bin/clientProtocol.o src/clientProtocol.cpp
	g++ $(CFLAGS) -o bin/clientEncoderDecoder.o src/clientEncoderDecoder.cpp
 
link: include/BGSclient.o include/clientConnectionHandler.o include/readFromKeyboardTask.o include/readFromSocketTask.o include/clientProtocol.o include/clientEncoderDecoder.o 
	g++ $(CFLAGS) -o bin/BGSclient bin/BGSclient.o bin/clientConnectionHandler.o bin/readFromKeyboardTask.o bin/readFromSocketTask.o bin/clientProtocol.o bin/clientEncoderDecoder.o

# bin/connectionHandler.o: src/connectionHandler.cpp
# 	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

# bin/echoClient.o: src/echoClient.cpp
# 	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
.PHONY: clean

clean:
	rm -f bin/*
