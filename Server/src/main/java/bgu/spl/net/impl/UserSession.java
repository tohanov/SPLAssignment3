package bgu.spl.net.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.net.impl.messages.ServerToClientMessage;

public class UserSession {
    
    private String username;
    private String password;
    private String birthday;

    private int numberOfPosts;
    private int numberOfFollowing;
    private AtomicInteger numberOfFollowers;

    private AtomicInteger sessionId;
    private ConcurrentLinkedDeque<String> followers;    //followers by username
    private ConcurrentLinkedDeque<ServerToClientMessage> receivedMessages;
    private HashSet<String> blockedUsers; // users which are blocked by this user

    public UserSession(String username,String password,String birthday){
        this.username=username;
        this.password=password;
        this.birthday=birthday;
        sessionId.set(-1);
        
        followers = new ConcurrentLinkedDeque<>(); 
        receivedMessages = new ConcurrentLinkedDeque<>();   //FIXME: concurrency issues
        blockedUsers= new HashSet<>();

        numberOfPosts=0;
        numberOfFollowing=0;
        numberOfFollowers = new AtomicInteger(0);
    }


    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;        
    }

    public String getBirthday(){
        return birthday;
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
   
    public boolean addfollower(UserSession userSessionToAdd){
        String username=userSessionToAdd.getUsername();
        
        if(followers.contains(username))
            return false;
            
        followers.add(username);
        numberOfFollowers.incrementAndGet();
        userSessionToAdd.increaseFollowing();

        return true;
    }

    public boolean removeFollower(UserSession userSessionToRemove){
        String username=userSessionToRemove.getUsername();
        boolean isRemoved=false;
       
        if(followers.remove(username)){
            numberOfFollowers.decrementAndGet(); 
            userSessionToRemove.decreaseFollowing(); 
            isRemoved=true;
        }
        
        return isRemoved;
       
    }
    
    public ConcurrentLinkedDeque<String> getFollowers(){
        return followers;

    }

    public void increaseFollowing(){
        ++numberOfFollowing;
    }

    public void decreaseFollowing(){
        --numberOfFollowing;
    }

    public void increaseNumOfPosts(){
        ++numberOfPosts;
    }

    public int getNumOfFollwing(){
        return numberOfFollowing;
    }

    public int getNumOfFollwers(){
        return numberOfFollowers.get();
    }

    public int getNumOfPosts(){
        return numberOfPosts;
    }

    public boolean isFollowedByThisUser(String followedUsername){
        return followers.contains(followedUsername);
    }

    public void blockUser(UserSession userSessionToBlock){
       String usernameToBlock=userSessionToBlock.getUsername();

       blockedUsers.add(usernameToBlock);
       removeFollower(userSessionToBlock);
       userSessionToBlock.removeFollower(this); 
              
    }

    public boolean isBlockingOtherUser(String otherUser){
        return blockedUsers.contains(otherUser);
    }

    public HashSet<String> getBlockedUsers(){
        return blockedUsers;
    }

    public ConcurrentLinkedDeque<ServerToClientMessage> getReceivedMessages(){
        return receivedMessages;

    }

    @Override
    public boolean equals(Object other){
        return username.equals(((UserSession) other).getUsername());
    }


    public int getAge() {
        String[] bithdayByParts=getBirthday().split("-");
        LocalDate present= LocalDate.now();
        return Period.between(LocalDate.of(Integer.parseInt(bithdayByParts[2]),
        		Integer.parseInt(bithdayByParts[1]),
        		Integer.parseInt(bithdayByParts[0])), present).getYears();

    }
    


}
