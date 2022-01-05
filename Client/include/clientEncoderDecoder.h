#ifndef SPLASSIGNMENT3_CLIENTENCDEC_H
#define SPLASSIGNMENT3_CLIENTENCDEC_H

using namespace std;



template <typename T>
class clientEncoderDecoder{
public:
    T decodeNextByte(char nextByte);

    char* encode(T message);

};


#endif //SPLASSIGNMENT3_CLIENTENCDEC_H
