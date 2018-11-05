package edu.floridapoly.polycamsportal.schedule;

import java.util.*;

public class Schedule {

    private List<CourseSection> sections;

    public Schedule(List<CourseSection> sections) {
        this.sections = sections;
    }

    public static List<Schedule> generateSchedules(List<Course> courses) {
        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        // Temporary object to avoid reallocating a schedule for each product
        Schedule current = new Schedule(Arrays.asList(
            new CourseSection[courses.size()]));
        for (int i = 0; i < courses.size(); i++) {
            List<CourseSection> sections = courses.get(i).getSections();
            if (sections.isEmpty()) {
                // If any course has no sections, there are no possible
                // schedules
                // TODO Throw exceptions if no courses can be found?
                return Collections.emptyList();
            } else {
                // Initialize to the first section of each course
                current.sections.set(i, sections.get(0));
            }
        }

        ArrayList<Schedule> schedules = new ArrayList<>();

        // Indices into the courses[i].getSections() lists
        int[] indices = new int[courses.size()];

        PRODUCT_LOOP:
        do {
            if (!current.hasConflicts()) {
                schedules.add(current.clone());
            }

            // Increment the first index and 'carry' the overflow to the next
            // course until it carries off the last course, meaning that all
            // products have been iterated over.
            for (int i = 0; i < courses.size(); i++) {
                List<CourseSection> sections = courses.get(i).getSections();
                if (++indices[i] < sections.size()) {
                    current.sections.set(i, sections.get(indices[i]));
                    // Don't propagate the carry if there is no overflow
                    break;
                } else if (i == courses.size() - 1) {
                    break PRODUCT_LOOP;
                } else {
                    indices[i] = 0;
                    current.sections.set(i, sections.get(0));
                }
            }
        } while (true);

        // TODO Throw an exception if there are not any available?
        // I guess that's the only way to return a message describing what
        // conflicts exist
        return schedules;
    }

    /**
     * Determines whether any sections in this schedule overlap.
     * <p>
     * TODO Return a message describing which sections overlap?
     *
     * @return true if any two sections overlap; false otherwise.
     */
    public boolean hasConflicts() {
        for (int i = 0; i < sections.size(); i++) {
            for (int j = i + 1; j < sections.size(); j++) {
                if (sections.get(i).overlaps(sections.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Schedule clone() {
        return new Schedule(new ArrayList<>(sections));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(sections, schedule.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "sections=" + sections +
            '}';
    }

}
