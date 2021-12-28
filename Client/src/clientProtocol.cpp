//
// Created by USER on 28/12/2021.
//

#include "clientProtocol.h"

    clientProtocol::clientProtocol() {
            shouldTErminate=false;
    }

    void clientProtocol::process(T message){
        short opCode = getOpCode(message);

        switch(opCode){
            case 9:

            case 10:

            case 11:

        }

    }

    short clientProtocol::getOpCode(T message){
    return;
}




