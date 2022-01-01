import bgu.spl.net.application.UserSession;

public class UnfollowMessage {
    
    private String username;

    public UnfollowMessage(String username){
        this.username=username;
    }

    public Message act(UserSession currentUserSession, Connections connection, HashMap<String,UserSession> usernameToUserSession){

        if(!currentUserSession.isLoggedIn()||!currentUserSession.removeFollower(username))
            return new ErrorMessage();

        return new AckMessage();    

    }
}
