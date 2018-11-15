package edu.floridapoly.polycamsportal.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.floridapoly.polycamsportal.util.TimeUtils;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Objects;

public class CourseSection {

    private int sectionNumber;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<CourseSession> sessions;

    /**
     * Reference to the course this section belongs to.
     */
    private Course course;

    /**
     * Initializes all values to default values.
     * <p>
     * This only exists so that JSON deserialization will work. It should not
     * be used in actual code.
     */
    @SuppressWarnings("unused")
    public CourseSection() {
        this(0, null, null, null);
    }

    /**
     * Constructs a CourseSection object.
     *
     * @param sectionNumber Number of this section.
     * @param startDate     LocalDate describing the starting date.
     * @param endDate       LocalDate describing the ending date.
     * @param sessions      Array of CourseSessions in this section.
     */
    public CourseSection(int sectionNumber,
                         LocalDate startDate, LocalDate endDate,
                         List<CourseSession> sessions) {
        this.sectionNumber = sectionNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessions = sessions;
    }

    @JsonProperty("section")
    public int getSectionNumber() {
        return sectionNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonProperty
    @SuppressWarnings("unused")
    private void setStartDate(int startDate) {
        this.startDate = TimeUtils.dateFromTimestamp(startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @JsonProperty
    @SuppressWarnings("unused")
    private void setEndDate(int endDate) {
        this.endDate = TimeUtils.dateFromTimestamp(endDate);
    }

    public List<CourseSession> getSessions() {
        return sessions;
    }

    // This has to be ignored during (de)serialization since JSON can't
    // handle circular references
    @JsonIgnore
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course for this section.
     * <p>
     * This can only be called once and should only be called when set as a
     * section of a Course object.
     *
     * @param course The course to set.
     */
    void setCourse(Course course) {
        if (this.course != null) {
            throw new IllegalStateException(
                "A sections's course can only be set once");
        }

        this.course = course;
    }

    /**
     * Determines whether any sessions of this section overlap with the sessions
     * of another section.
     *
     * @param other Other course section.
     * @return true if the times of this and other overlap; false otherwise.
     */
    public boolean overlaps(CourseSection other) {
        for (CourseSession session : sessions) {
            for (CourseSession otherSession : other.getSessions()) {
                if (session.overlaps(otherSession)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSection that = (CourseSection) o;
        return sectionNumber == that.sectionNumber &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(sessions, that.sessions);
        // course is not compared since it is a circular reference
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionNumber, startDate, endDate, sessions);
    }

    @Override
    public String toString() {
        return "CourseSection{" +
            "sectionNumber=" + sectionNumber +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", sessions=" + sessions +
            '}';
    }

}
