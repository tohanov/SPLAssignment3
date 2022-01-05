CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: newClient
	g++ -o bin/echoExample bin/connectionHandler.o bin/echoClient.o $(LDFLAGS) 

# EchoClient: bin/connectionHandler.o bin/echoClient.o

newClient: compile link

compile: src/NewClient.cpp
	g++ $(CFLAGS) -o bin/NewClient.o src/NewClient.cpp -O3
 
link: include/NewClient.o
	g++ $(CFLAGS) -o bin/newClient bin/NewClient.o 

# bin/connectionHandler.o: src/connectionHandler.cpp
# 	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

# bin/echoClient.o: src/echoClient.cpp
# 	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
.PHONY: clean

clean:
	rm -f bin/*
