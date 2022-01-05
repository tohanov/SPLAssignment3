package bgu.spl.net.impl.messages;

public class NotificationMessage extends ServerToClientMessage {

    private String postingUser;
    private String content;

    public NotificationMessage(int messageOpCode, String postingUser, String content){
        super(messageOpCode,9);
        this.postingUser=postingUser;
        this.content=content;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }

    /**
     * 
     * @return true if public post, false if PM
     */
    public boolean isPublicPost(){
        return getMessageOpCode()==5;
    }


    
    
}
