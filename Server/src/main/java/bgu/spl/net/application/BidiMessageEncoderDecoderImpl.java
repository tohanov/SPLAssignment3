import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import Messages.LogoutMessage;
import Messages.LogstatMessage;
import Messages.Message;
import Messages.RegisterMessage;
import bgu.spl.net.api.MessageEncoderDecoder;


public class BidiMessageEncoderDecoderImpl implements MessageEncoderDecoder<Message>{

    private byte[] bytes= new byte[1 << 10];
    private int len;

    public BidiMessageEncoderDecoderImpl(){

        bytes= new byte[1 << 10];
        len=0;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            return returnCompleteMessage();
        }

        pushByte(nextByte);
        return null; //not a line yet
        
    }

    @Override
    public byte[] encode(Message message) {
        // TODO Auto-generated method stub
        return null;
    }

    private Message returnCompleteMessage(){

        int opCode= getOpCode(bytes);

        switch (opCode) {
            case 1: // register
                return createRegisterMessage();
                
        
            case 2: // login
                
                break;

            case 3: // logout
                return new LogoutMessage();
                
            case 4: // follow/unollow
                return createFollowUnfollowMessage();
                

            case 5: // post
                return new createPostMessage();             

            case 6: // private message
                
                break;

            case 7: //logstat
                return new LogstatMessage();
                
            case 8: //stat
                
                break;

            case 9: //notification
                
                break;

            case 10: //Ack   
                
                break;

            case 11:  //Error
                
                break;

            case 12:  //block
                
                break;

                

            
            default:
                break;
        }



    }
    
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    public static short getOpCode(byte[] byteArr)

    {

    short result = (short)((byteArr[0] & 0xff) << 8);

    result += (short)(byteArr[1] & 0xff);

    return result;

    }
    //-----------------------------------------

    private Message createRegisterMessage() {
        String[] result = (new String(bytes, 2, len-2, StandardCharsets.UTF_8)).split("\0");  //2 is offset in order to remove the opCode
        len=0;
               
        return new RegisterMessage(result[0], result[1], result[2]);

    }

    private Message createFollowUnfollowMessage(){
        String username = new String(bytes, 3 , len-3, StandardCharsets.UTF_8);
        len=0;

        if(bytes[2]==0)
            return new FollowMessage(username);
        
        else return new UnfollowMessage(username);

    }

    public Message createPostMessage(){
        String messageToPost = new String(bytes, 2 , len-2, StandardCharsets.UTF_8);
        len=0;

        ArrayList<String> targetUsers=new ArrayList<>();

        String[] words=messageToPost.split(" ");
        for (String word : words) {
            if(word.length()>1 && word.charAt(0)=='@') // hashtag and username
                targetUsers.add(word.substring(1));
            
        }

        return new PostMessage(messageToPost,targetUsers);
    }
}


//     private byte[] bytes = new byte[1 << 10]; //start with 1024
//     private int len = 0;

//     @Override
//     public String decodeNextByte(byte nextByte) {
//         //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
//         //this allow us to do the following comparison
//         if (nextByte == '\n') {
//             return popString();
//         }

//         pushByte(nextByte);
//         return null; //not a line yet
//     }

//     @Override
//     public byte[] encode(String message) {
//         return (message + "\n").getBytes(); //uses utf8 by default
//     }

//     private void pushByte(byte nextByte) {
//         if (len >= bytes.length) {
//             bytes = Arrays.copyOf(bytes, len * 2);
//         }

//         bytes[len++] = nextByte;
//     }

//     private String popString() {
//         //notice that we explicitly requesting that the string will be decoded from UTF-8
//         //this is not actually required as it is the default encoding in java.
//         String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
//         len = 0;
//         return result;
//     }