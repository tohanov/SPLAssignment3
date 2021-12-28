//
// Created by USER on 28/12/2021.
//

#ifndef SPLASSIGNMENT3_CLIENTPROTOCOL_H
#define SPLASSIGNMENT3_CLIENTPROTOCOL_H

template <typename T>
class clientProtocol{

private:
    bool shouldTerminate;
public:

    clientProtocol(){}

    void process(T message);
    bool shouldTerminate();
    short getOpCode(T message);



};


#endif //SPLASSIGNMENT3_CLIENTPROTOCOL_H
