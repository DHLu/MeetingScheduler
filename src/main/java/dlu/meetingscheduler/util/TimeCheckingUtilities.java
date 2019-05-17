package dlu.meetingscheduler.util;

import java.time.LocalTime;
import java.util.Objects;

public class TimeCheckingUtilities {

    public static boolean notBefore(LocalTime a, LocalTime b) {

        Objects.requireNonNull(a, "a");
        Objects.requireNonNull(b, "b");

        return a.compareTo(b) >= 0;
    }

    public static boolean notAfter(LocalTime a, LocalTime b) {

        Objects.requireNonNull(a, "a");
        Objects.requireNonNull(b, "b");

        return a.compareTo(b) <= 0;
    }
}
