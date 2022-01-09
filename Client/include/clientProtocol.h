#ifndef SPLASSIGNMENT3_CLIENTPROTOCOL_H
#define SPLASSIGNMENT3_CLIENTPROTOCOL_H


#include "clientProgramStages.h"


using namespace std;


class ConnectionHandler; // forwared declaration

template <typename T> class clientProtocol {
	private:
		bool should_terminate;
		ConnectionHandler *ptr_handler;

	public:
		clientProtocol(ConnectionHandler *_ptr_handler);

		void process(T message);
		bool shouldTerminate();
};


#endif //SPLASSIGNMENT3_CLIENTPROTOCOL_H
