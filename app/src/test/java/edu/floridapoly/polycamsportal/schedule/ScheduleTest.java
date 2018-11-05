package edu.floridapoly.polycamsportal.schedule;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ScheduleTest {

    private static final Course COURSE_MOBILE_APPS = new Course(
        "Mobile Device Applications", "COP", "4656", "I&T", 3,
        Arrays.asList(
            new CourseSection(
                1,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Topsakal, Oguzhan", "IST-1026", DaySet.MWF,
                        new LocalTime(8, 0), new LocalTime(8, 50)
                    )
                )
            )
        )
    );

    private static final Course COURSE_PROG_2 = new Course(
        "Computer Programming 2", "COP", "3330C", "I&T", 3,
        Arrays.asList(
            new CourseSection(
                1,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Elish, Karim", "IST-1028", DaySet.MW,
                        new LocalTime(10, 0), new LocalTime(11, 50)
                    )
                )
            ),
            new CourseSection(
                2,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Elish, Karim", "IST-1028", DaySet.MW,
                        new LocalTime(13, 0), new LocalTime(14, 50)
                    )
                )
            )
        )
    );

    private static final Course COURSE_CIRCUITS_2 = new Course(
        "Circuits 2", "EEL", "3112C", "ENGR", 3,
        Arrays.asList(
            new CourseSection(
                1,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Alsweiss, Suleiman", "IST-1056",
                        DaySet.M,
                        new LocalTime(10, 0),
                        new LocalTime(11, 50)
                    ),
                    new CourseSession(
                        "Alsweiss, Suleiman", "IST-1014",
                        DaySet.TR,
                        new LocalTime(9, 30),
                        new LocalTime(14, 50)
                    )
                )
            ),
            new CourseSection(
                2,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Alsweiss, Suleiman", "IST-1056",
                        DaySet.M,
                        new LocalTime(13, 0),
                        new LocalTime(14, 50)
                    ),
                    new CourseSession(
                        "Alsweiss, Suleiman", "IST-1014",
                        DaySet.TR,
                        new LocalTime(9, 30),
                        new LocalTime(14, 50)
                    )
                )
            )
        )
    );

    private static final Course COURSE_PROB_STAT = new Course(
        "Probability and Statistics", "STA", "3032", "GEMTH", 3,
        Arrays.asList(
            new CourseSection(
                1,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Alnaser, Ala' Jamil", "IST-1045",
                        DaySet.TR,
                        new LocalTime(11, 0),
                        new LocalTime(12, 45)
                    )
                )
            ),
            new CourseSection(
                2,
                new LocalDate(2018, 8, 22),
                new LocalDate(2018, 12, 5),
                Arrays.asList(
                    new CourseSession(
                        "Alnaser, Ala' Jamil", "IST-1045",
                        DaySet.TR,
                        new LocalTime(15, 30),
                        new LocalTime(16, 45)
                    )
                )
            )
        )
    );


    @Test
    public void noCoursesNoSchedules() {
        assertThat(Schedule.generateSchedules(Collections.<Course>emptyList()),
            is(Collections.<Schedule>emptyList()));
    }

    @Test
    public void noSectionsNoSchedules() {
        assertThat(Schedule.generateSchedules(Arrays.asList(
            COURSE_MOBILE_APPS, COURSE_PROG_2, COURSE_CIRCUITS_2, COURSE_PROB_STAT
        )).size(), is(2));
    }

}
