#ifndef SPLASSIGNMENT3_CLIENTPROTOCOL_H
#define SPLASSIGNMENT3_CLIENTPROTOCOL_H


using namespace std;


template <typename T> class clientProtocol {
	private:
		bool should_terminate;

	public:
		clientProtocol();

		void process(T message);
		bool shouldTerminate();
};


#endif //SPLASSIGNMENT3_CLIENTPROTOCOL_H
