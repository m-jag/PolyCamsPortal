package edu.floridapoly.polycamsportal.schedule;

import java.util.List;
import java.util.Objects;

public class Course {

    private String title;
    private String department;
    // This is a String because some courses numbers have X and C in them
    private String number;
    private String type;

    private int credits;

    private List<CourseSection> sections;

    @SuppressWarnings("unused")
    public Course() {
        this("", "", "", "", 0, null);
    }

    public Course(String title, String department, String number, String type,
                  int credits, List<CourseSection> sections) {
        this.title = title;
        this.department = department;
        this.number = number;
        this.type = type;
        this.credits = credits;
        this.setSections(sections);
    }

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public int getCredits() {
        return credits;
    }

    public List<CourseSection> getSections() {
        return sections;
    }

    /**
     * Sets the list of sections for this course.
     * <p>
     * Each section is given a reference to this course so that it can access
     * its metadata. Because of this, the list of sections must not be
     * "owned" by another Course object, as an exception will be thrown
     * otherwise.
     * <p>
     * Note: This is private, but is called by Jackson upon deserialization.
     *
     * @param sections List of sections to set.
     */
    @SuppressWarnings("unused")
    private void setSections(List<CourseSection> sections) {
        if (sections != null) {
            for (CourseSection section : sections) {
                section.setCourse(this);
            }
        }

        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return credits == course.credits &&
            Objects.equals(title, course.title) &&
            Objects.equals(department, course.department) &&
            Objects.equals(number, course.number) &&
            Objects.equals(type, course.type) &&
            Objects.equals(sections, course.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, department, number, type, credits, sections);
    }

    @Override
    public String toString() {
        return "Course{" +
            "title='" + title + '\'' +
            ", department='" + department + '\'' +
            ", number='" + number + '\'' +
            ", type='" + type + '\'' +
            ", credits=" + credits +
            ", sections=" + sections +
            '}';
    }

}
