package entity;

public class UserProfile {
    private final String username;
    private final int userId;

    public UserProfile(String un, int id){
        this.username = un;
        this.userId = id;
    }

    public String getCurrentUserName(){
        return username;
    }

    public int getCurrentUserId(){
        return userId;
    }
}
