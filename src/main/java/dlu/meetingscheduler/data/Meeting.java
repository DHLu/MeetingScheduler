package dlu.meetingscheduler.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Meeting {

    private String employeeId;

    private LocalDate requestDate;

    private LocalTime requestTime;

    private LocalDate meetingDate;

    private LocalTime startingTime;

    private LocalTime endingTime;

    public Meeting(String employeeId, LocalDate requestDate, LocalTime requestTime, LocalDate meetingDate, LocalTime startingTime, LocalTime endingTime) {
        this.employeeId = employeeId;
        this.requestDate = requestDate;
        this.requestTime = requestTime;
        this.meetingDate = meetingDate;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getRequestDateTime() {
        return LocalDateTime.of(requestDate, requestTime);
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("%s %s %s", startingTime.format(timeFormatter), endingTime.format(timeFormatter), employeeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(employeeId, meeting.employeeId) &&
                Objects.equals(requestDate, meeting.requestDate) &&
                Objects.equals(requestTime, meeting.requestTime) &&
                Objects.equals(meetingDate, meeting.meetingDate) &&
                Objects.equals(startingTime, meeting.startingTime) &&
                Objects.equals(endingTime, meeting.endingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, requestDate, requestTime, meetingDate, startingTime, endingTime);
    }
}
