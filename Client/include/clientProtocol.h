//
// Created by USER on 28/12/2021.
//

#ifndef SPLASSIGNMENT3_CLIENTPROTOCOL_H
#define SPLASSIGNMENT3_CLIENTPROTOCOL_H


class clientProtocol<T> {

private:
    boolean shouldTerminate;
public:

    clientProtocol(){}

    void process(T message);
    boolean shouldTerminate();
    short getOpCode(T message);



};


#endif //SPLASSIGNMENT3_CLIENTPROTOCOL_H
