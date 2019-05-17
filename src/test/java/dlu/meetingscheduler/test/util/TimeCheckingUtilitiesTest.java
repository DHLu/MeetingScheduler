package dlu.meetingscheduler.test.util;

import dlu.meetingscheduler.util.TimeCheckingUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeCheckingUtilitiesTest {

    private static LocalTime dummyTime;

    @BeforeAll
    static void init() {
        dummyTime = LocalTime.parse("12:00", DateTimeFormatter.ISO_LOCAL_TIME);
    }

    private static Stream<Arguments> notBeforeTestDataSource() {

        LocalTime time = dummyTime;

        return Stream.of(
                Arguments.arguments(time, time, true, null),
                Arguments.arguments(time, time.minusNanos(1), true, null),
                Arguments.arguments(time, time.minusSeconds(1), true, null),
                Arguments.arguments(time, time.minusMinutes(1), true, null),
                Arguments.arguments(time, time.minusHours(1), true, null),
                Arguments.arguments(time, time.plusNanos(1), false, null),
                Arguments.arguments(time, time.plusSeconds(1), false, null),
                Arguments.arguments(time, time.plusMinutes(1), false, null),
                Arguments.arguments(time, time.plusHours(1), false, null),
                Arguments.arguments(time, null, false, NullPointerException.class),
                Arguments.arguments(null, time, false, NullPointerException.class),
                Arguments.arguments(null, null, false, NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("notBeforeTestDataSource")
    void notBeforeTest(LocalTime a, LocalTime b, boolean notBefore, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(notBefore, TimeCheckingUtilities.notBefore(a, b));
        } else {
            assertThrows(clazz, () -> TimeCheckingUtilities.notBefore(a, b));
        }
    }

    private static Stream<Arguments> notAfterTestDataSource() {

        LocalTime time = dummyTime;
        return Stream.of(
                Arguments.arguments(time, time, true, null),
                Arguments.arguments(time, time.plusNanos(1), true, null),
                Arguments.arguments(time, time.plusSeconds(1), true, null),
                Arguments.arguments(time, time.plusMinutes(1), true, null),
                Arguments.arguments(time, time.plusHours(1), true, null),
                Arguments.arguments(time, time.minusNanos(1), false, null),
                Arguments.arguments(time, time.minusSeconds(1), false, null),
                Arguments.arguments(time, time.minusMinutes(1), false, null),
                Arguments.arguments(time, time.minusHours(1), false, null),
                Arguments.arguments(time, null, false, NullPointerException.class),
                Arguments.arguments(null, time, false, NullPointerException.class),
                Arguments.arguments(null, null, false, NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("notAfterTestDataSource")
    void notAfterTest(LocalTime a, LocalTime b, boolean notAfter, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(notAfter, TimeCheckingUtilities.notAfter(a, b));
        } else {
            assertThrows(clazz, () -> TimeCheckingUtilities.notAfter(a, b));
        }
    }
}
