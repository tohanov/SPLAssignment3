package bgu.spl.net.application.messages;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.application.UserSession;

public class LogstatMessage extends ClientToServerMessage {

    public class UserStats {
        private int age;
        private int numberOfPosts;
        private int numberOfFollowers;
        private int numberOfFollowing;

        public UserStats(int age, int numberOfPosts, int numberOfFollowers, int numberOfFollowing) {
            this.age = age;
            this.numberOfPosts = numberOfPosts;
            this.numberOfFollowers = numberOfFollowers;
            this.numberOfFollowing = numberOfFollowing;
        }

    }

    @Override
    public ClientToServerMessage act(UserSession currentUserSession, Connections connections, HashMap<String, UserSession> usernameToUserSession) {
        
        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return new ErrorMessage();

        ArrayList<UserStats> output = new ArrayList<>();

        for(UserSession user: usernameToUserSession.values()){
            if(user.isLoggedIn()){
                output.add(new UserStats(user.getAge(), user.getNumOfPosts(), user.getNumOfFollwers(), user.getNumOfFollwing()));
            }

           
        }

        return new LogStatAckMessage(output);
    }   
}
