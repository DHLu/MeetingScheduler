package dlu.meetingscheduler.service;

import dlu.meetingscheduler.data.Meeting;
import dlu.meetingscheduler.data.OfficeHour;
import dlu.meetingscheduler.util.TimeCheckingUtilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FileParsingManager {

    private static final DateTimeFormatter OFFICE_HOUR = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter REQUEST_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter MEETING_TIME = DateTimeFormatter.ofPattern("HH:mm");

    private static final String DELIMITER = " ";
    private static final int OFFICE_HOUR_SPLIT_PARTS = 2;
    private static final int MEETING_SPLIT_PARTS = 3;

    private static final int HOURS_OF_DAY = 24;

    public OfficeHour getOfficeHour(String officeHour) {

        Objects.requireNonNull(officeHour, "officeHour");

        String[] parsedTime = officeHour.split(DELIMITER);
        checkLineFormat(parsedTime, OFFICE_HOUR_SPLIT_PARTS);

        LocalTime startingTime = LocalTime.parse(parsedTime[0], OFFICE_HOUR);
        LocalTime closingTime = LocalTime.parse(parsedTime[1], OFFICE_HOUR);

        checkTimePeriod(startingTime, closingTime, null);

        return new OfficeHour(startingTime, closingTime);
    }

    public Meeting getMeeting(String meetingLineOne, String meetingLineTwo) {

        Objects.requireNonNull(meetingLineOne, "meetingLineOne");
        Objects.requireNonNull(meetingLineTwo, "meetingLineTwo");

        String[] parsedMeetingLineOne = meetingLineOne.split(DELIMITER);
        checkLineFormat(parsedMeetingLineOne, MEETING_SPLIT_PARTS);

        String[] parsedMeetingLineTwo = meetingLineTwo.split(DELIMITER);
        checkLineFormat(parsedMeetingLineTwo, MEETING_SPLIT_PARTS);

        LocalDate requestDate = LocalDate.parse(parsedMeetingLineOne[0], DATE);
        LocalTime requestTime = LocalTime.parse(parsedMeetingLineOne[1], REQUEST_TIME);
        String employeeId = parsedMeetingLineOne[2];

        LocalDate meetingDate = LocalDate.parse(parsedMeetingLineTwo[0], DATE);
        LocalTime meetingStartingTime = LocalTime.parse(parsedMeetingLineTwo[1], MEETING_TIME);
        int duration = Integer.parseInt(parsedMeetingLineTwo[2]);
        LocalTime meetingEndingTime = meetingStartingTime.plusHours(duration);

        checkTimePeriod(meetingStartingTime, meetingEndingTime, duration);

        return new Meeting(employeeId, requestDate, requestTime, meetingDate, meetingStartingTime, meetingEndingTime);
    }

    // according to the desired output in the requirement,
    // the office hour and meeting time should be at the same day and within 24 hours.
    private void checkTimePeriod(LocalTime start, LocalTime end, Integer duration) {
        if ((duration != null && duration > HOURS_OF_DAY) || TimeCheckingUtilities.notBefore(start, end)) {
            throw new IllegalArgumentException("The time period is not correct!");
        }
    }

    private void checkLineFormat(String[] parsedValue, int expectedLength) {
        if (parsedValue.length != expectedLength) {
            throw new IllegalArgumentException("The file content is not in a correct format!");
        }
    }
}
