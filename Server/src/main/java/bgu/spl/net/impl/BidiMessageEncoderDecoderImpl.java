package bgu.spl.net.impl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;
import bgu.spl.net.impl.messages.LogstatMessage.UserStats;


public class BidiMessageEncoderDecoderImpl implements MessageEncoderDecoder<Message>{
    private byte[] bytes= new byte[1 << 10];
    private int len;


    public BidiMessageEncoderDecoderImpl(){

		bytes = new byte[1 << 10];
		len = 0;
    }


    @Override
    public Message decodeNextByte(byte nextByte) {
		System.out.println("[*] decodeNextByte()");

        if (nextByte == ';') {
			System.out.println("[*] decodeNextByte(), got byte ';'");
            return returnCompleteMessage();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }


    @Override
    public byte[] encode(Message message) {
		System.out.println("[*] encode()");
        int opCode = message.getOpCode();    // checks whether notification (9), ack(10), error(11)
        
        if(opCode==9){
            return encodeNotification(message);
        }

        else if(opCode==10){
            return encodeAck(message);
        }

        else{ //opCode==11
            return encodeError(message);
        }
    }
    

    private ClientToServerMessage returnCompleteMessage(){
        int opCode= getOpCode(bytes);
		System.out.println("[*] returnCompleteMessage(), opCode=" + opCode);
		ClientToServerMessage completeMessage;

        switch (opCode) {
            case 1: // register
				completeMessage =  createRegisterMessage();
				break;
                
        
            case 2: // login
				completeMessage =  createLoginMessage();
				break;
            

            case 3: // logout
				completeMessage =  new LogoutMessage();
				break;
                
            case 4: // follow/unollow
				completeMessage =  createFollowUnfollowMessage();
				break;
                

            case 5: // post
				completeMessage =  createPostMessage();         
				break;    

            case 6: // private message
				completeMessage =  createPM_Message();        
				break;      

            case 7: //logstat
				completeMessage =  new LogstatMessage();
				break;
                
            case 8: //stat
				completeMessage =  createStatMessage();
				break;

            case 12:  //block
                completeMessage = createBlockMessage();  
				break;
				
			default:
				completeMessage = null;
        }

		len = 0; //FIXME
		return completeMessage;
    }

    
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }


    public static short getOpCode(byte[] byteArr) {
		short result = (short)((byteArr[0] & 0xff) << 8);

		result += (short)(byteArr[1] & 0xff);

		return result;
    }
    //-----------------------------------------

    private ClientToServerMessage createRegisterMessage() {
		System.out.println("[*] createRegisterMessage()");
        String[] result = (new String(bytes, 2, len-3, StandardCharsets.UTF_8)).split("\0");  //2 is offset in order to remove the opCode
        len=0;
        
        return new RegisterMessage(result[0], result[1], result[2]);
    }

    private ClientToServerMessage createLoginMessage(){
        String[] result = (new String(bytes, 2, len-2, StandardCharsets.UTF_8)).split("\0");  //2 is offset in order to remove the opCode
        len=0;

		System.out.println(Arrays.deepToString(result));

		byte captcha = 0;
		if (result.length == 3)
			captcha = (byte)(result[2].charAt(0));

        return new LoginMessage(result[0], result[1],  captcha);

    }

    private ClientToServerMessage createFollowUnfollowMessage(){
        String username = new String(bytes, 3 , len-4, StandardCharsets.UTF_8);
        len=0;

        if(bytes[2]==0)
            return new FollowMessage(username);
        
        else return new UnfollowMessage(username);

    }

    private ClientToServerMessage createPostMessage(){
        String messageToPost = new String(bytes, 2 , len-3, StandardCharsets.UTF_8);
        len=0;

        ArrayList<String> targetUsers=new ArrayList<>();

        String[] words=messageToPost.split(" ");
        for (String word : words) {
            if(word.length()>1 && word.charAt(0)=='@') // hashtag and username
                targetUsers.add(word.substring(1));
            
        }

        return new PostMessage(messageToPost,targetUsers);
    }

    private ClientToServerMessage createPM_Message(){
        String messageToPost = new String(bytes, 2 , len-3, StandardCharsets.UTF_8);
        len=0;

        String[] messagByParts = messageToPost.split("\0");

        return new PM_Message(messagByParts[0], messagByParts[1], messagByParts[2]);

    }

    private ClientToServerMessage createStatMessage(){
        String usernamesString = new String(bytes, 2 , len-3, StandardCharsets.UTF_8);
        len=0;

        return new StatMessage(usernamesString.split("\\|"));

    }

    private ClientToServerMessage createBlockMessage(){
        String userToBlock = new String(bytes, 2 , len-3, StandardCharsets.UTF_8);
        len=0;

        return new BlockMessage(userToBlock);

    }
    //-----------------------------------------------------

    private byte[] encodeNotification(Message message){

        byte[] opCodeBytes = shortToBytes((short)9); 
        byte notificationType = (byte)(((NotificationMessage)message).isPublicPost() ? 1 : 0);
        byte[] postingUser=((NotificationMessage)message).getPostingUser().getBytes();
        byte[] content= ((NotificationMessage)message).getContent().getBytes();
        
        int size= 2 + 1 + postingUser.length + 1 + content.length + 1 + 1;
		int index=0;

        byte[] output = new byte[size];

        System.arraycopy(opCodeBytes, 0, output, index, opCodeBytes.length);
        index+=opCodeBytes.length;

        output[index++]=notificationType;

        System.arraycopy(postingUser, 0, output, index, postingUser.length);
        index+=postingUser.length;

        output[index++] = 0;

        System.arraycopy(content, 0, output, index, content.length);
        index+=content.length;

        output[index++] = 0;
		output[index] = ';';

        return output;
    }


    @SuppressWarnings("unchecked")
    private byte[] encodeAck(Message message) {

        short messageOpCode = ((ServerToClientMessage) message).getMessageOpCode();
		System.out.println("[*] encodeAck(), opCode=" + messageOpCode);
        
        byte[] opCodeBytes = shortToBytes((short)10); // Ack opcode
        byte[] messageOpCodeBytes = shortToBytes(messageOpCode); // the opcode of the message the ack is responding to
        byte[] output;

        if(messageOpCode == 4){  // if responding to follow
            byte[] info = (( (String) ((AckMessage) message).getInformation())).getBytes();

			// format: [ack opcode] [msg opcode] [follow/unfollow byte] [name]
            output = new byte[2 + 2 + 1 + info.length + 1]; 
			
            System.arraycopy(opCodeBytes, 0, output, 0, 2);
            System.arraycopy(messageOpCodeBytes, 0, output, 2, 2);
			// FIXME check if nullbyte ruins string
            System.arraycopy(info, 0, output, 4 /* 5 */, info.length);

        }

        else if(messageOpCode == 7 || messageOpCode == 8){ // stat or logstat ack
            ArrayList<UserStats> statsList = (ArrayList<UserStats>)(((AckMessage) message).getInformation());
            output = new byte[2 + 2 + 8 * statsList.size() + 1]; // each message is 8 bytes, except for the first which contains additional 4 bytes for opcode and messageOpCode. 
            
            System.arraycopy(opCodeBytes, 0, output, 0, 2);
            System.arraycopy(messageOpCodeBytes, 0, output, 2, 2);

            int index= 4;
            for(UserStats userStats : statsList){
                byte[] temp = userStatsToBytes(userStats);

                System.arraycopy(temp, 0, output, index, temp.length );
                index+=temp.length; // 8
            }
        }

        else{
            output = new byte[4 + 1]; 
            System.arraycopy(opCodeBytes, 0, output, 0, 2);
            System.arraycopy(messageOpCodeBytes, 0, output, 2, 2);
        }

		output[output.length - 1] = ';';
        return output;
    }


    private byte[] encodeError(Message message) {
        short messageOpCode = ((ServerToClientMessage) message).getMessageOpCode(); // opcode of message that error responds to
        
        byte[] opCodeBytes = shortToBytes((short)11); 
        byte[] messageOpCodeBytes = shortToBytes(messageOpCode);
        
        byte[] output = new byte[5]; 
        System.arraycopy(opCodeBytes, 0, output, 0, 2);
        System.arraycopy(messageOpCodeBytes, 0, output, 2, 2);

		output[4] = ';';
        return output;
        
    }

    

    //-----------------------------------------------------

    public byte[] shortToBytes(short num) {

        byte[] bytesArr = new byte[2];

        bytesArr[0] = (byte)((num >> 8) & 0xFF);

        bytesArr[1] = (byte)(num & 0xFF);

        return bytesArr;

    }

    private byte[] userStatsToBytes(UserStats userStats){
        byte[] output= new byte[8];

        byte[][] info = {
            shortToBytes((short)userStats.getAge()),
            shortToBytes((short)userStats.getNumberOfPosts()),
            shortToBytes((short)userStats.getNumberOfFollowers()),
            shortToBytes((short)userStats.getNumberOfFollowing())
        };

        int index = 0;
        for (byte[] bytesArray : info) {
            System.arraycopy(bytesArray, 0, output, index, bytesArray.length);
            index += bytesArray.length; // 2
        }
        
        return output;
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