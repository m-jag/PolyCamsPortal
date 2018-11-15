package edu.floridapoly.polycamsportal.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class TimeUtils {

    private TimeUtils() {
    }

    /**
     * Gets a LocalTime instance from a timestamp containing the number of
     * seconds since the start of the day.
     *
     * @param timestamp Number of seconds since the start of the day.
     * @return LocalTime instance corresponding to timestamp.
     */
    public static LocalTime timeFromTimestamp(int timestamp) {
        int seconds = timestamp % 60;
        int minutes = timestamp / 60 % 60;
        int hours = timestamp / 3600;
        return new LocalTime(hours, minutes, seconds);
    }

    public static LocalDate dateFromTimestamp(int timestamp) {
        return new LocalDate((long) timestamp * 1000);
    }

}
