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
            contents = init(args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        OfficeHour officeHour = fileParsingManager.getOfficeHour(contents.get(0));

        PriorityQueue<Meeting> sortedRequest = new PriorityQueue<>(Comparator.comparing(Meeting::getRequestDateTime));

        Map<LocalDate, PriorityQueue<Meeting>> meetings = new HashMap<>();
        for (int lineNumber = 1; lineNumber < contents.size(); lineNumber += 2) {

            Meeting newMeeting = fileParsingManager.getMeeting(contents.get(lineNumber), contents.get(lineNumber + 1));

            if (!meetingManager.isValidMeeting(newMeeting, officeHour)) {
                continue;
            }

            sortedRequest.add(newMeeting);
        }

        for (Meeting newMeeting : sortedRequest) {

            PriorityQueue<Meeting> meetingsOfTheDay = meetings.get(newMeeting.getMeetingDate());

            if (meetingsOfTheDay == null) {

                meetingsOfTheDay = new PriorityQueue<>(Comparator.comparing(Meeting::getStartingTime));
                meetingsOfTheDay.add(newMeeting);
                meetings.put(newMeeting.getMeetingDate(), meetingsOfTheDay);

            } else {
                boolean occupied = meetingsOfTheDay.stream().anyMatch(existedMeeting -> meetingManager.isTimeSlotOccupied(existedMeeting, newMeeting));

                if (!occupied) {
                    meetingsOfTheDay.add(newMeeting);
                }
            }
        }

        meetings.keySet().stream().sorted().forEach(localDate -> {
            System.out.println(localDate);
            PriorityQueue<Meeting> meetingsOfTheDay = meetings.get(localDate);
            meetingsOfTheDay.forEach(System.out::println);
        });

    }

    private static List<String> init(String[] args) throws IOException {

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
