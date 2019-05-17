package dlu.meetingscheduler.test.service;

import dlu.meetingscheduler.data.Meeting;
import dlu.meetingscheduler.data.OfficeHour;
import dlu.meetingscheduler.service.FileParsingManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileParsingManagerTest {

    private static FileParsingManager fileParsingManager;

    @BeforeAll
    static void init() {
        fileParsingManager = new FileParsingManager();
    }

    private static Stream<Arguments> getOfficeHourDataSource() {

        return Stream.of(

                // correct input value and format
                Arguments.arguments("0600 1800", new OfficeHour(LocalTime.parse("06:00"), LocalTime.parse("18:00")), null),

                // wrong input for office hour rule (at the same day and within 24 hours)
                Arguments.arguments("0600 0500", null, IllegalArgumentException.class),

                // null value as input
                Arguments.arguments(null, null, NullPointerException.class),

                // wrong input format
                Arguments.arguments("0312", null, IllegalArgumentException.class),
                Arguments.arguments("03120312", null, IllegalArgumentException.class),
                Arguments.arguments("0312 0312 0312", null, IllegalArgumentException.class),

                // wrong time format
                Arguments.arguments("312 312", null, DateTimeParseException.class),
                Arguments.arguments("03:12 03:12", null, DateTimeParseException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("getOfficeHourDataSource")
    void getOfficeHourTest(String officeHourString, OfficeHour officeHour, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(officeHour, fileParsingManager.getOfficeHour(officeHourString));
        } else {
            assertThrows(clazz, () -> fileParsingManager.getOfficeHour(officeHourString));
        }
    }

    private static Stream<Arguments> getMeetingDataSource() {

        return Stream.of(

                // correct input value and format
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        new Meeting("EMPLOYEE_ID", LocalDate.parse("2000-01-01"), LocalTime.parse("06:00:00"),
                                LocalDate.parse("2000-01-02"), LocalTime.parse("09:00:00"), LocalTime.parse("11:00:00")),
                        null),
                Arguments.arguments(
                        "2000-01-01 06:32:11 EMPLOYEE_ID",
                        "2000-01-02 09:00 12",
                        new Meeting("EMPLOYEE_ID", LocalDate.parse("2000-01-01"), LocalTime.parse("06:32:11"),
                                LocalDate.parse("2000-01-02"), LocalTime.parse("09:00:00"), LocalTime.parse("21:00:00")),
                        null),

                // null value as input
                Arguments.arguments(null, null, null, NullPointerException.class),
                Arguments.arguments("2000-01-01 06:00:00 EMPLOYEE_ID", null, null, NullPointerException.class),
                Arguments.arguments(null, "2000-01-02 09:00 2", null, NullPointerException.class),

                // wrong input format
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID EXTRA_CONTENT",
                        "2000-01-02 09:00 2",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01T06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 2 EXTRA_CONTENT",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00-2",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02T09:00 2",
                        null, IllegalArgumentException.class),

                // wrong input for meeting hour rule (at the same day and within 24 hours)
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 24",
                        null, IllegalArgumentException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 23:01 1",
                        null, IllegalArgumentException.class),

                // wrong date or time format
                Arguments.arguments(
                        "2000/01/01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        null, DateTimeParseException.class),
                Arguments.arguments(
                        "2000-01-01 06:00 EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        null, DateTimeParseException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 09:00:00 2",
                        null, DateTimeParseException.class),
                Arguments.arguments(
                        "2000-01-01 06:00:00 EMPLOYEE_ID",
                        "2000-01-02 0900 2",
                        null, DateTimeParseException.class),
                Arguments.arguments(
                        "2000-01-01 060000 EMPLOYEE_ID",
                        "2000-01-02 09:00 2",
                        null, DateTimeParseException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("getMeetingDataSource")
    void getMeetingTest(String meetingLineOne, String meetingLineTwo, Meeting meeting, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(meeting, fileParsingManager.getMeeting(meetingLineOne, meetingLineTwo));
        } else {
            assertThrows(clazz, () -> fileParsingManager.getMeeting(meetingLineOne, meetingLineTwo));
        }
    }
}
