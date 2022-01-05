//
// Created by USER on 28/12/2021.
//

#include "clientProtocol.h"

template <typename T>
clientProtocol<T>::clientProtocol():shouldTerminate(false){}
    
template <typename T>
void clientProtocol<T>::process(T message){
    short opCode = getOpCode(message);

    switch(opCode){
        case 9:

        case 10:

        case 11:

    }

}


template <typename T>
short clientProtocol<T>::getOpCode(T message){
    return;
}




