package se.lexicon.controller;

import se.lexicon.dao.CalendarDao;
import se.lexicon.dao.MeetingDao;
import se.lexicon.dao.UserDao;
import se.lexicon.exception.CalendarExceptionHandler;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.Meeting;
import se.lexicon.model.MeetingCalendar;
import se.lexicon.model.User;
import se.lexicon.view.CalendarView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class CalendarController {

    //Dependencies
    private CalendarView view;
    private UserDao userDao;
    private CalendarDao calendarDao;
    private MeetingDao meetingDao;
    //Fields
    private boolean isLoggedIn;
    private String username;

    //Constructor

   public CalendarController(CalendarView view, UserDao userDao, CalendarDao calendarDao, MeetingDao meetingDao) {
    this.view = view;
    this.userDao = userDao;
    this.calendarDao = calendarDao;
    this.meetingDao = meetingDao;
}

    public void run() {
        while (true) {
            view.displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 0:
                    register();
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    createCalendar();
                    break;
                case 3:
                    createMeeting();
                    break;
                case 4:
                    deleteCalendar();
                    break;
                case 5:
                    deleteMeeting();
                    break;
                case 6:
                    displayCalendar();
                    break;
                case 7:
                    displayMeetings();
                    break;
                case 8:
                    updateMeetings();
                    break;
                case 9:
                    isLoggedIn = false;
                    break;
                case 10:
                    System.exit(0);
                    break;

                default:
                    view.displayWarningMessage("Invalid choice. Please select a valid option.");
            }

        }
    }

    private int getUserChoice() {
        String operationType = view.promoteString();
        int choice = -1;
        try {
            choice = Integer.parseInt(operationType);
        } catch (NumberFormatException e) {
            view.displayErrorMessage("Invalid Input. Please enter a number.");
        }
        return choice;
    }

    private void register() {
        view.displayMessage("Enter your username");
        String username = view.promoteString();
        User registerdUser = userDao.createUser(username);
        view.displayUser(registerdUser);
    }

    private void login() {
        User user = view.promoteUserForm();
        try {
            isLoggedIn = userDao.authenticate(user);
            username = user.getUsername();
            view.displaySuccessMessage("Login successful.");
        } catch (Exception e) {
            CalendarExceptionHandler.handelException(e);
        }
    }

    private void createCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        String calendarTitle = view.promoteCalendarForm();
        MeetingCalendar createdMeetingCalendar = calendarDao.createCalender(calendarTitle, username);
        view.displaySuccessMessage("Calendar created successfully.");
        view.displayCalendar(createdMeetingCalendar);
    }

    private void createMeeting() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        view.displayMessage("Enter calendar id you want to add meeting to");
        int calendarId = scanner.nextInt();
        Meeting meeting = view.promoteMeetingForm();
        Optional<MeetingCalendar> meetingCalendarOptional = calendarDao.findById(calendarId);
        if (!meetingCalendarOptional.isPresent()){
                throw new MySQLException("No calendar with the id: " + calendarId + " found.");
        } else {
            meeting.setMeetingCalendar(meetingCalendarOptional.get());
            Meeting createdMeeting = meetingDao.createMeeting(meeting);
            view.displaySuccessMessage("Meeting created successfully");
        }

    }

    private void deleteCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter calendar id to delete");
        int removeId = scanner.nextInt();
        Optional<MeetingCalendar> calendar = calendarDao.findById(removeId);
        if (!calendar.isPresent()) {
            view.displayErrorMessage("Calendar with id: " + removeId);
        } else {
            calendarDao.deleteCalender(removeId);
            view.displaySuccessMessage("Calendar deleted successfully.");
        }
    }

    private void deleteMeeting() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the meeting you want to delete");
        int id = scanner.nextInt();
        boolean isDeleted = meetingDao.deleteMeeting(id);
        if (isDeleted) {
            view.displaySuccessMessage("Meeting deleted successfully.");
        } else {
            view.displayErrorMessage("No meeting found with the id: " + id);
        }
    }

    public void displayCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Collection<MeetingCalendar> allCalendars = calendarDao.findByUsername(username);
        allCalendars.forEach(view::displayCalendar);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the calendar you want to display");
        int id = scanner.nextInt();
        Optional<MeetingCalendar> calendar = calendarDao.findById(id);
        if (!calendar.isPresent()) {
            view.displayErrorMessage("No calendar found with the id: " + id);
        } else {
            Collection<Meeting> meetings = meetingDao.findAllMeetingsByCalenderId(id);
            view.displayMeetings(new ArrayList<>(meetings));
        }
    }

    private void updateMeetings() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the meeting you want to update");
        int id = scanner.nextInt();
        Optional<Meeting> meeting = meetingDao.findById(id);
        if (!meeting.isPresent()) {
            view.displayErrorMessage("No meeting found with the id: " + id);
        } else {
            Meeting updatedMeeting = view.promoteMeetingForm();
            boolean isUpdated = meetingDao.updateMeeting(id, updatedMeeting); // Pass the ID to the updateMeeting method
            if (isUpdated) {
                view.displaySuccessMessage("Meeting updated successfully.");
            } else {
                view.displayErrorMessage("Failed to update the meeting.");
            }
        }

    }

    private void displayMeetings() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the calendar you want to display meetings from");
        int id = scanner.nextInt();
        Optional<MeetingCalendar> calendar = calendarDao.findById(id);
        if (!calendar.isPresent()) {
            view.displayErrorMessage("No calendar found with the id: " + id);
        } else {
            Collection<Meeting> meetings = meetingDao.findAllMeetingsByCalenderId(id);
            view.displayMeetings(new ArrayList<>(meetings));
        }
    }
}
