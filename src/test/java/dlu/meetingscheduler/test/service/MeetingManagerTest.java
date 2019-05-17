package dlu.meetingscheduler.test.service;

import dlu.meetingscheduler.data.Meeting;
import dlu.meetingscheduler.data.OfficeHour;
import dlu.meetingscheduler.service.MeetingManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MeetingManagerTest {

    private static MeetingManager meetingManager;
    private static LocalDate dummyDate;
    private static LocalTime dummyStartingTime;
    private static LocalTime dummyEndingTime;

    @BeforeAll
    static void init() {
        meetingManager = new MeetingManager();
        dummyDate = LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
        dummyStartingTime = LocalTime.parse("06:00", DateTimeFormatter.ISO_LOCAL_TIME);
        dummyEndingTime = LocalTime.parse("18:00", DateTimeFormatter.ISO_LOCAL_TIME);
    }

    private static Meeting getMeeting(LocalTime startingTime, LocalTime endingTime) {
        return new Meeting("employeeId", dummyDate, dummyStartingTime, dummyDate, startingTime, endingTime);
    }

    private static Stream<Arguments> isValidMeetingDataSource() {

        LocalTime startingTime = dummyStartingTime;
        LocalTime endingTime = dummyEndingTime;
        return Stream.of(

                // meeting time is INSIDE of office time
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        new OfficeHour(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, startingTime),
                        new OfficeHour(startingTime, startingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime.minusHours(1)),
                        new OfficeHour(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime.plusHours(1), endingTime),
                        new OfficeHour(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        new OfficeHour(startingTime.minusHours(1), endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        new OfficeHour(startingTime, endingTime.plusHours(1)),
                        true, null),

                // meeting time is OUTSIDE of office time
                Arguments.arguments(
                        getMeeting(startingTime.minusNanos(1), endingTime),
                        new OfficeHour(startingTime, endingTime),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime.plusNanos(1)),
                        new OfficeHour(startingTime, endingTime),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        new OfficeHour(startingTime.plusNanos(1), endingTime),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        new OfficeHour(startingTime, endingTime.minusNanos(1)),
                        false, null),

                // null value as input
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        null,
                        false, NullPointerException.class),
                Arguments.arguments(
                        null,
                        new OfficeHour(startingTime, endingTime),
                        false, NullPointerException.class),
                Arguments.arguments(
                        null,
                        null,
                        false, NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("isValidMeetingDataSource")
    void isValidMeetingTest(Meeting meeting, OfficeHour officeHour, boolean valid, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(valid, meetingManager.isValidMeeting(meeting, officeHour));
        } else {
            assertThrows(clazz, () -> meetingManager.isValidMeeting(meeting, officeHour));
        }
    }

    private static Stream<Arguments> isTimeSlotOccupiedDataSource() {

        LocalTime startingTime = dummyStartingTime;
        LocalTime endingTime = dummyEndingTime;
        return Stream.of(

                // meeting time IS NOT overlapped
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(endingTime, endingTime.plusNanos(1)),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime.minusNanos(1), startingTime),
                        getMeeting(startingTime, endingTime),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(endingTime.plusNanos(1), endingTime.plusNanos(2)),
                        false, null),
                Arguments.arguments(
                        getMeeting(startingTime.minusNanos(2), startingTime.minusNanos(1)),
                        getMeeting(startingTime, endingTime),
                        false, null),

                // meeting time IS overlapped
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime.minusNanos(1), endingTime.plusNanos(1)),
                        getMeeting(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(startingTime.minusNanos(1), endingTime.plusNanos(1)),
                        true, null),
                Arguments.arguments(
                        getMeeting(endingTime.minusNanos(1), endingTime.plusNanos(1)),
                        getMeeting(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime.minusNanos(1), startingTime.plusNanos(1)),
                        getMeeting(startingTime, endingTime),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(startingTime.minusNanos(1), startingTime.plusNanos(1)),
                        true, null),
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        getMeeting(endingTime.minusNanos(1), endingTime.plusNanos(1)),
                        true, null),

                // null value as input
                Arguments.arguments(
                        getMeeting(startingTime, endingTime),
                        null,
                        true, NullPointerException.class),
                Arguments.arguments(
                        null,
                        getMeeting(startingTime, endingTime),
                        true, NullPointerException.class),
                Arguments.arguments(
                        null,
                        null,
                        true, NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("isTimeSlotOccupiedDataSource")
    void isTimeSlotOccupiedTest(Meeting existedMeeting, Meeting newMeeting, boolean occupied, Class<Exception> clazz) {

        if (clazz == null) {
            assertEquals(occupied, meetingManager.isTimeSlotOccupied(existedMeeting, newMeeting));
        } else {
            assertThrows(clazz, () -> meetingManager.isTimeSlotOccupied(existedMeeting, newMeeting));
        }
    }
}
