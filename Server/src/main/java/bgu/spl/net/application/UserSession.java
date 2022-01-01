package bgu.spl.net.application;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class UserSession {
    
    private String username;
    private String password;
    private String birthday;
    private AtomicInteger sessionId;
    private ConcurrentLinkedDeque<String> followers;    //followers by username
    private ConcurrentLinkedDeque<Byte[]> receivedMessages;

    public UserSession(String username,String password,String birthday){
        this.username=username;
        this.password=password;
        this.birthday=birthday;
        sessionId.set(-1);
        followers = new ConcurrentLinkedDeque<>(); 
        receivedMessages = new ConcurrentLinkedDeque<>();   //FIXME: concurrency issues
    }


    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;        
    }

    public boolean setSessionId(Integer connectionId) {
        return sessionId.compareAndSet(-1, connectionId);
    }

    public Integer getSessionId(){
        return sessionId.get();
    }

    public boolean isLoggedIn(){
        return sessionId.get() != -1;
    }

    public void resetSessionId(){   
        sessionId.set(-1);
    }
   
    public boolean addfollower(String username){
        if(followers.contains(username))
            return false;
            
        followers.add(username);
        return true;
    }

    public boolean removeFollower(String username){
       return followers.remove(username);
    }
    
    public ConcurrentLinkedDeque<String> getFollowers(){
        return followers;

    }

    public ConcurrentLinkedDeque<Byte[]> getReceivedMessages(){
        return receivedMessages;

    }

    @Override
    public boolean equals(Object other){
        return username.equals(((UserSession) other).getUsername());
    }


}
