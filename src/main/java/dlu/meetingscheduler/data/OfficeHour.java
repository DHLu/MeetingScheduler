package dlu.meetingscheduler.data;

import java.time.LocalTime;
import java.util.Objects;

public class OfficeHour {

    private final LocalTime startingTime;

    private final LocalTime closingTime;

    public OfficeHour(LocalTime startingTime, LocalTime closingTime) {
        this.startingTime = startingTime;
        this.closingTime = closingTime;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeHour that = (OfficeHour) o;
        return Objects.equals(startingTime, that.startingTime) &&
                Objects.equals(closingTime, that.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingTime, closingTime);
    }
}
