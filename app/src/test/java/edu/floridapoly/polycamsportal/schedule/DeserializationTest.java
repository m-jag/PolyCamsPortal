package edu.floridapoly.polycamsportal.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class DeserializationTest {

    private static final CourseSession MOBILE_APPS_SESSION =
        new CourseSession(
            "Topsakal, Oguzhan", "IST-1026", DaySet.MWF,
            new LocalTime(8, 0), new LocalTime(8, 50));

    // Test date to compare against.
    // This is separated so that different levels can be compared against
    private static final List<CourseSession> MOBILE_APPS_SESSIONS =
        Collections.singletonList(MOBILE_APPS_SESSION);
    private static final CourseSection MOBILE_APPS_SECTION = new CourseSection(
        1, new LocalDate(2018, 8, 22), new LocalDate(2018, 12, 5),
        MOBILE_APPS_SESSIONS);
    private static final List<CourseSection> MOBILE_APPS_SECTIONS =
        Collections.singletonList(MOBILE_APPS_SECTION);
    private static final Course MOBILE_APPS_COURSE = new Course(
        "Mobile Device Applications", "COP", "4656", "I&T", 3,
        MOBILE_APPS_SECTIONS);
    /**
     * Just create one of these for all the tests since it doesn't store any
     * state.
     */
    private static ObjectMapper mapper = new ObjectMapper();

    private static CourseSession readSingleSession() throws IOException {
        return mapper.readValue(
            DeserializationTest.class.getClassLoader().getResource(
                "schedule/single_session.json"),
            CourseSession.class);
    }

    private static CourseSection readSingleSection() throws IOException {
        return mapper.readValue(
            DeserializationTest.class.getClassLoader().getResource(
                "schedule/single_section.json"),
            CourseSection.class);
    }

    private static Course readSingleCourse() throws IOException {
        return mapper.readValue(
            DeserializationTest.class.getClassLoader().getResource(
                "schedule/single_course.json"),
            Course.class);
    }

    private static List<Course> readAllCourses() throws IOException {
        return Arrays.asList(mapper.readValue(
            DeserializationTest.class.getClassLoader().getResource(
                "schedule/all_courses.json"),
            Course[].class));
    }

    @Test
    public void courseSessionDeserialize() throws IOException {
        CourseSession session = readSingleSession();

        assertThat(session.getInstructor(), is("Topsakal, Oguzhan"));
        assertThat(session.getRoom(), is("IST-1026"));
        assertThat(session.getDays(), is(DaySet.MWF));
        assertThat(session.getStartTime(), is(new LocalTime(8, 0)));
        assertThat(session.getEndTime(), is(new LocalTime(8, 50)));
    }

    @Test
    public void courseSectionDeserialize() throws IOException {
        CourseSection section = readSingleSection();

        assertThat(section.getSectionNumber(), is(1));
        assertThat(section.getStartDate(), is(new LocalDate(2018, 8, 22)));
        assertThat(section.getEndDate(), is(new LocalDate(2018, 12, 5)));

        assertThat(section.getSessions(), is(MOBILE_APPS_SESSIONS));
    }

    @Test
    public void courseDeserialize() throws IOException {
        Course course = readSingleCourse();

        assertThat(course.getTitle(), is("Mobile Device Applications"));
        assertThat(course.getDepartment(), is("COP"));
        assertThat(course.getNumber(), is("4656"));
        assertThat(course.getType(), is("I&T"));
        assertThat(course.getCredits(), is(3));
        assertThat(course.getSections(), is(MOBILE_APPS_SECTIONS));
    }

    @Test
    public void courseListDeserialize() throws IOException {
        List<Course> courses = readAllCourses();

        // This and the lack of errors should be enough to verify that
        // everything works
        assertThat(courses.size(), is(126));
        assertThat(courses, hasItem(MOBILE_APPS_COURSE));
    }

}
