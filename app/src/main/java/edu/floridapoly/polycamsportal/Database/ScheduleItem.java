package edu.floridapoly.polycamsportal.Database;

/*
public static final String SCHEDULE_COL_ID = "ID";
public static final String SCHEDULE_COL_USER = "user";
public static final String SCHEDULE_COL_NAME = "name";
*/

public class ScheduleItem {
    private String title;
    private String name;
    private String user;

    public ScheduleItem() {
        this("Schedule", "user");
    }

    public ScheduleItem(String title, String user) {
        this.title = title;
        this.name = title;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }
    public String getName() {
        return title;
    }
    public String getUser() {
        return user;
    }
}
