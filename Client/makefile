executable_name:=BGSclient

# compile_flags:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -O3 -pthread
# link_flags:=-lboost_system -lpthread -lstdc++ -lboost_thread 

compile_flags:=-c -Iinclude -pthread -std=c++11 -O3 -Wall -Weffc++ -g
link_flags:=-lboost_system -lboost_thread -lpthread
object_files:=			bin/BGSclient.o \
				bin/clientConnectionHandler.o \
				bin/readFromKeyboardTask.o \
				bin/readFromSocketTask.o \
				bin/clientProtocol.o \
				bin/clientEncoderDecoder.o


# all: clear compile link
all: clear clean compile link


clear:
	clear


clean:
	echo "[*] Cleaning..."
	rm -f bin/*
	touch bin/.gitkeep


compile: client
	# echo "[*] Compiling..."


link: $(object_files)
	echo "[*] Linking everything..."
	g++ $(link_flags) -o bin/$(executable_name) $(object_files)


check: bin/studio ExampleInput.txt #TODO
	echo "Running with valgrind..."
	valgrind --leak-check=full --show-reachable=yes bin/studio ExampleInput.txt


checkV: bin/studio ExampleInput.txt #TODO
	echo "Running with valgrind with -v flag..."
	valgrind -v --leak-check=full --show-reachable=yes bin/studio ExampleInput.txt


client: handler tasks src/BGSclient.cpp
	echo "[*] Compiling client..."
	g++ $(compile_flags) -o bin/BGSclient.o src/BGSclient.cpp


handler: src/clientConnectionHandler.cpp src/clientProtocol.cpp src/clientEncoderDecoder.cpp
	echo "[*] Compiling handler..."
	g++ $(compile_flags) -o bin/clientConnectionHandler.o src/clientConnectionHandler.cpp
	g++ $(compile_flags) -o bin/clientProtocol.o src/clientProtocol.cpp
	g++ $(compile_flags) -o bin/clientEncoderDecoder.o src/clientEncoderDecoder.cpp


tasks: src/readFromKeyboardTask.cpp src/readFromSocketTask.cpp
	echo "[*] Compiling tasks..."
	g++ $(compile_flags) -o bin/readFromKeyboardTask.o src/readFromKeyboardTask.cpp
	g++ $(compile_flags) -o bin/readFromSocketTask.o src/readFromSocketTask.cpp


.PHONY: clean


ifndef VERBOSE
.SILENT:
endif


# all: clean compile link
# g++ $(link_flags) -o bin/echoExample bin/connectionHandler.o bin/echoClient.o

# bin/connectionHandler.o: src/connectionHandler.cpp
# 	g++ $(compile_flags) -o bin/connectionHandler.o src/connectionHandler.cpp

# bin/echoClient.o: src/echoClient.cpp
# 	g++ $(compile_flags) -o bin/echoClient.o src/echoClient.cpp

# EchoClient: bin/connectionHandler.o bin/echoClient.o
