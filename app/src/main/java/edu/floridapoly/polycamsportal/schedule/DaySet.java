package edu.floridapoly.polycamsportal.schedule;

import java.util.EnumSet;
import java.util.Objects;

public class DaySet {
    // Some common day sets, primarily for use in tests.
    public static final DaySet M = new DaySet(EnumSet.of(Day.Monday));
    public static final DaySet W = new DaySet(EnumSet.of(Day.Wednesday));
    public static final DaySet F = new DaySet(EnumSet.of(Day.Friday));
    public static final DaySet MW = new DaySet(
        EnumSet.of(Day.Monday, Day.Wednesday));
    public static final DaySet MWF = new DaySet(
        EnumSet.of(Day.Monday, Day.Wednesday, Day.Friday));
    public static final DaySet TR = new DaySet(
        EnumSet.of(Day.Tuesday, Day.Thursday));

    private final EnumSet<Day> days;

    public DaySet() {
        this(EnumSet.noneOf(Day.class));
    }

    public DaySet(String days) {
        this.days = EnumSet.noneOf(Day.class);
        for (int i = 0; i < days.length(); i++) {
            this.days.add(Day.fromChar(days.charAt(i)));
        }
    }

    public DaySet(EnumSet<Day> days) {
        this.days = days;
    }

    public EnumSet<Day> getDays() {
        return days;
    }

    public boolean overlaps(DaySet other) {
        for (Day day : days) {
            if (other.getDays().contains(day)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaySet daySet = (DaySet) o;
        return Objects.equals(days, daySet.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(days);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // TODO Make the days appear in the correct order
        // (since EnumSet's order is implementation-defined)
        for (Day day : days) {
            sb.append(day.toChar());
        }
        return sb.toString();
    }

}
