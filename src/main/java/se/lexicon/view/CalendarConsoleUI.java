package se.lexicon.view;

import se.lexicon.model.MeetingCalendar;
import se.lexicon.model.Meeting;
import se.lexicon.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CalendarConsoleUI implements CalendarView {
    @Override
    public void displayUser(User user) {
        System.out.println(user.userInfo());
        System.out.println("--------------------");
    }

    @Override
    public void displayCalendar(MeetingCalendar meetingCalendar) {
        System.out.println(meetingCalendar.calendarInfo());
        System.out.println("------------------------");;
    }

    @Override
    public void displayMeetings(List<Meeting> meetings) {
        if (meetings.isEmpty()) {
            System.out.println("No meetings in this calendar.");
        } else {
            System.out.println("Meetings in this calendar: ");
            meetings.forEach(meeting -> System.out.println(meeting.meetingInfo()));
        }
    }

    @Override
    public String promoteString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public Meeting promoteMeetingForm() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a title");
        String title =  scanner.nextLine();
        System.out.println("Start Date & Time (yyyy-MM-dd HH:mm): ");
        String start = scanner.nextLine();
        System.out.println("End Date & Time (yyyy-MM-dd HH:mm): ");
        String end = scanner.nextLine();

        System.out.println("Description:");
        String desc = scanner.nextLine();

        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new Meeting(title, startDateTime, endDateTime, desc);
    }

    @Override
    public String promoteCalendarForm() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a title: ");
        return scanner.nextLine();
    }

    @Override
    public User promoteUserForm() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a username");
        String username = scanner.nextLine();
        System.out.println("Enter password");
        String password = scanner.nextLine();
        return new User(username, password);
    }
}
