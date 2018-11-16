package edu.floridapoly.polycamsportal.util;

public class ScheduleItem {
    private String title;
    private String time;
    private String room;
    private String professor;

    @SuppressWarnings("unused")
    public ScheduleItem() {
        this("Class", "00:00", "0000", "John Doe");
    }

    public ScheduleItem(String title, String time, String room, String professor) {
        this.title = title;
        this.time = time;
        this.room = room;
        this.professor = professor;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getProfessor() {
        return professor;
    }
}
