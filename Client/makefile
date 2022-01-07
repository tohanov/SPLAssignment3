executable_name:=BGSclient

compile_flags:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -O3 -pthread
link_flags:=-lboost_system -lpthread
object_files:=	bin/BGSclient \
				bin/BGSclient.o \
				bin/clientConnectionHandler.o \
				bin/readFromKeyboardTask.o \
				bin/readFromSocketTask.o \
				bin/clientProtocol.o \
				bin/clientEncoderDecoder.o


all: clear compile link
# all: clean compile link


clear:
	clear


clean:
	echo "[*] Cleaning..."
	rm -f bin/*


compile: client
	echo "[*] Compiling..."


link: $(object_files)
	echo "[*] Linking everything..."
	g++ $(compile_flags) $(link_flags) -o bin/$(executable_name) $(object_files)


check: bin/studio ExampleInput.txt
	echo "Running with valgrind..."
	valgrind --leak-check=full --show-reachable=yes bin/studio ExampleInput.txt


checkV: bin/studio ExampleInput.txt
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
