#ifndef STAGES__
#define STAGES__


namespace ClientProgramStages {
	enum LogoutStages {
		NOT_LOGGED_OUT,
		// WAITING_FOR_ACK,
		LOGGED_OUT
	};

	// enum LoginStages {
	// 	NOT_LOGGED_IN,
	// 	WAITING_FOR_ACK,
	// 	LOGGED_IN
	// };

	enum MessageStages {
		WAITING_TO_SEND,
		WAITING_FOR_RESPONSE
	};
}


#endif