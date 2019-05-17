package dlu.meetingscheduler.service;

import dlu.meetingscheduler.data.Meeting;
import dlu.meetingscheduler.data.OfficeHour;
import dlu.meetingscheduler.util.TimeCheckingUtilities;

import java.util.Objects;

public class MeetingManager {

    public boolean isValidMeeting(Meeting meeting, OfficeHour officeHour) {

        Objects.requireNonNull(meeting, "meeting");
        Objects.requireNonNull(officeHour, "officeHour");

        return TimeCheckingUtilities.notBefore(meeting.getStartingTime(), officeHour.getStartingTime())
                && TimeCheckingUtilities.notAfter(meeting.getEndingTime(), officeHour.getClosingTime());
    }

    public boolean isTimeSlotOccupied(Meeting existedMeeting, Meeting newMeeting) {

        Objects.requireNonNull(existedMeeting, "existedMeeting");
        Objects.requireNonNull(newMeeting, "newMeeting");

        return (
                (TimeCheckingUtilities.notBefore(newMeeting.getStartingTime(), existedMeeting.getStartingTime())
                        && newMeeting.getStartingTime().isBefore(existedMeeting.getEndingTime()))
                ||
                (newMeeting.getEndingTime().isAfter(existedMeeting.getStartingTime())
                        && TimeCheckingUtilities.notAfter(newMeeting.getEndingTime(), existedMeeting.getEndingTime()))
                ||
                (newMeeting.getStartingTime().isBefore(existedMeeting.getStartingTime())
                        && newMeeting.getEndingTime().isAfter(existedMeeting.getEndingTime()))
                );
    }
}
