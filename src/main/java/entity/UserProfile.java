package entity;

public class UserProfile {
    private String username;
    private int userId;

    public UserProfile(String un, int id){
        this.username = un;
        this.userId = id;
    }

    public String getCurrentUser(){
        return username;
    }

    public int getCurrentUserId(){
        return userId;
    }
}
