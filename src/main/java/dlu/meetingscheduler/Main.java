package dlu.meetingscheduler;

import dlu.meetingscheduler.data.Meeting;
import dlu.meetingscheduler.data.OfficeHour;
import dlu.meetingscheduler.service.FileParsingManager;
import dlu.meetingscheduler.service.MeetingManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        FileParsingManager fileParsingManager = new FileParsingManager();
        MeetingManager meetingManager = new MeetingManager();

        List<String> contents;
        try {
            contents = readFile(args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        OfficeHour officeHour = fileParsingManager.getOfficeHour(contents.get(0));

        List<Meeting> sortedRequest = new ArrayList<>();

        for (int lineNumber = 1; lineNumber < contents.size(); lineNumber += 2) {

            Meeting newMeeting = fileParsingManager.getMeeting(contents.get(lineNumber), contents.get(lineNumber + 1));

            if (!meetingManager.isValidMeeting(newMeeting, officeHour)) {
                continue;
            }

            sortedRequest.add(newMeeting);
        }

        // sort the list with earliest request at the top.
        sortedRequest.sort(Comparator.comparing(Meeting::getRequestDateTime));

        // divide the meeting by the day
        // each day contains a list of meetings
        Map<LocalDate, List<Meeting>> meetings = new HashMap<>();

        for (Meeting newMeeting : sortedRequest) {

            List<Meeting> meetingsOfTheDay = meetings.get(newMeeting.getMeetingDate());

            // if there is no meeting at this date, directly add the meeting to the list.
            if (meetingsOfTheDay == null) {

                meetingsOfTheDay = new ArrayList<>();
                meetingsOfTheDay.add(newMeeting);
                meetings.put(newMeeting.getMeetingDate(), meetingsOfTheDay);

            } else {
                // if the time slot is not occupied, the meeting will be added to the list.

                boolean occupied = meetingsOfTheDay.stream().anyMatch(existedMeeting -> meetingManager.isTimeSlotOccupied(existedMeeting, newMeeting));

                if (!occupied) {
                    meetingsOfTheDay.add(newMeeting);
                }
            }
        }

        meetings.keySet().stream().sorted().forEach(localDate -> {
            System.out.println(localDate);
            meetings.get(localDate).stream()
                    .sorted(Comparator.comparing(Meeting::getStartingTime))
                    .forEach(System.out::println);
        });
    }

    private static List<String> readFile(String[] args) throws IOException {

        String filePath;

        if (args.length > 0) {
            filePath = args[0];
        } else {
            throw new IllegalArgumentException("The correct path to the file is missing containing the meeting information as the parameter!");
        }

        List<String> contents = Files.readAllLines(Paths.get(filePath));

        if (contents.isEmpty() || (contents.size() % 2) == 0) {
            throw new IllegalArgumentException("The file contains incorrect number of lines!");
        }

        return contents;
    }
}
