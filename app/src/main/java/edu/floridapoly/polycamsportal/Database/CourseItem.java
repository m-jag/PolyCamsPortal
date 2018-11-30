package edu.floridapoly.polycamsportal.Database;

public class CourseItem {
    private String title;
    private String starttime;
    private String endtime;
    private String room;
    private String professor;

    @SuppressWarnings("unused")
    public CourseItem() {
        this("Class", "00:00", "00:00","0000", "John Doe");
    }

    public CourseItem(String title, String starttime, String endtime, String room, String professor) {
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
        this.room = room;
        this.professor = professor;
    }

    public String getTitle() { return getName();    }
    public String getName() { return title; }
    public String getStartTime() {
        return starttime;
    }
    public String getEndTime() { return endtime; }
    public String getRoom() {
        return room;
    }
    public String getProfessor() {
        return professor;
    }
}
