package bgu.spl.net.impl.messages;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.bidi.Connections;
import bgu.spl.net.impl.UserSession;

public class LogstatMessage extends ClientToServerMessage {

    
    
    public LogstatMessage() {
        super(7);
    }

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

        public int getAge() {
            return age;
        }

        public int getNumberOfPosts() {
            return numberOfPosts;
        }

        public int getNumberOfFollowers() {
            return numberOfFollowers;
        }

        public int getNumberOfFollowing() {
            return numberOfFollowing;
        }

        
    }

    @Override
    public ServerToClientMessage act(int currentUserId, Connections<Message> connections, ConcurrentHashMap<String, UserSession> usernameToUserSession) {

        UserSession currentUserSession = connections.getHandler(currentUserId).getUserSession();

        if(currentUserSession==null || !currentUserSession.isLoggedIn())
            return error();

        ArrayList<UserStats> output = new ArrayList<>();

        for(UserSession user: usernameToUserSession.values()){
            if(user.isLoggedIn() && !user.isBlockingOtherUser(currentUserSession.getUsername())){
                output.add(new UserStats(user.getAge(), user.getNumOfPosts(), user.getNumOfFollwers(), user.getNumOfFollwing()));
            }

           
        }

        return ack(output);
    }   
}
