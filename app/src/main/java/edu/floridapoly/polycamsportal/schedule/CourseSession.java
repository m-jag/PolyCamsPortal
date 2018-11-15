package edu.floridapoly.polycamsportal.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.floridapoly.polycamsportal.util.TimeUtils;
import org.joda.time.LocalTime;

import java.util.Objects;

public class CourseSession {

    private String instructor;
    private String room;
    private DaySet days;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Initializes all values to default values.
     * <p>
     * This only exists so that JSON deserialization will work. It should not
     * be used in actual code.
     */
    @SuppressWarnings("unused")
    public CourseSession() {
        this(null, null, null, null, null);
    }

    /**
     * Constructs a CourseSession with timestamps instead of LocalTime
     * objects and a string specifying the days instead of a EnumSet&lt;
     * Day&gt; object.
     *
     * @param instructor Course instructor name, in the format "last, first".
     * @param room       Room number.
     * @param days       EnumSet of the days the session is on.
     * @param startTime  LocalTime describing the starting time.
     * @param endTime    LocalTime describing the ending time.
     */
    public CourseSession(String instructor, String room, DaySet days,
                         LocalTime startTime, LocalTime endTime) {
        this.instructor = instructor;
        this.room = room;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getRoom() {
        return room;
    }

    public DaySet getDays() {
        return days;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    @SuppressWarnings("unused")
    @JsonProperty
    private void setStartTime(int timestamp) {
        this.startTime = TimeUtils.timeFromTimestamp(timestamp);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @SuppressWarnings("unused")
    @JsonProperty
    private void setEndTime(int timestamp) {
        this.endTime = TimeUtils.timeFromTimestamp(timestamp);
    }

    /**
     * Determines whether a session overlaps with another one.
     *
     * @param other Other course session.
     * @return true if the times of this and other overlap; false otherwise.
     */
    public boolean overlaps(CourseSession other) {
        return days.overlaps(other.getDays())
            && startTime.compareTo(other.getEndTime()) <= 0
            && endTime.compareTo(other.getStartTime()) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSession that = (CourseSession) o;
        return Objects.equals(instructor, that.instructor) &&
            Objects.equals(room, that.room) &&
            Objects.equals(days, that.days) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructor, room, days, startTime, endTime);
    }

    @Override
    public String toString() {
        return "CourseSession{" +
            "instructor='" + instructor + '\'' +
            ", room='" + room + '\'' +
            ", days=" + days +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
