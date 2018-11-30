package edu.floridapoly.polycamsportal.Database;

public class UserItem {
    private String name;
    private String favoriteSchedule;

    public UserItem() {
        this( "User", null);
    }

    public UserItem(String name, String favoriteSchedule) {
        this.name = name;
        this.favoriteSchedule = favoriteSchedule;
    }

    public String getName() {
        return name;
    }
    public String getFavoriteSchedule() {
        return favoriteSchedule;
    }
}
