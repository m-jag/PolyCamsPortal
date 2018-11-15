package edu.floridapoly.polycamsportal.schedule;

public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    public static Day fromChar(char c) {
        switch (c) {
            case 'M':
                return Day.Monday;
            case 'T':
                return Day.Tuesday;
            case 'W':
                return Day.Wednesday;
            case 'R':
                return Day.Thursday;
            case 'F':
                return Day.Friday;
            case 'S':
                return Day.Saturday;
            case 'U':
                return Day.Sunday;
            default:
                throw new IllegalArgumentException(
                    "Character must be one of MTWRFSU.");
        }
    }

    public char toChar() {
        switch (this) {
            case Monday:
                return 'M';
            case Tuesday:
                return 'T';
            case Wednesday:
                return 'W';
            case Thursday:
                return 'R';
            case Friday:
                return 'F';
            case Saturday:
                return 'S';
            // This is kind of weird, but it shouldn't be used anyway since
            // there aren't any classes on Saturday or Sunday as far as I known.
            case Sunday:
                return 'U';
            default:
                return '\0'; // Unreachable
        }
    }
}
