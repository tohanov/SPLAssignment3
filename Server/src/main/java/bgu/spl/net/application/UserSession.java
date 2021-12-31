package main.java.bgu.spl.net.application;

import java.util.concurrent.atomic.AtomicInteger;

public class UserSession {
    
    private String username;
    private String password;
    private AtomicInteger sessionId;


    public UserSession(String username,String password){
        this.username=username;
        this.password=password;
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

   
    @Override
    public boolean equals(Object other){
        return username.equals(((UserSession) other).getUsername());
    }
}
