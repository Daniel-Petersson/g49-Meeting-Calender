package se.lexicon.controller;

import se.lexicon.dao.CalendarDao;
import se.lexicon.dao.MeetingDao;
import se.lexicon.dao.UserDao;
import se.lexicon.exception.CalendarExceptionHandler;
import se.lexicon.model.Meeting;
import se.lexicon.model.MeetingCalendar;
import se.lexicon.model.User;
import se.lexicon.view.CalendarView;

import javax.sound.midi.Soundbank;
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

    public CalendarController(CalendarView view, UserDao userDao, CalendarDao calendarDao) {
        this.view = view;
        this.userDao = userDao;
        this.calendarDao = calendarDao;
    }

    public void run(){
        while (true){
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
                    //todo: call display calendar method
                    break;
                case 6:
                    isLoggedIn = false;
                    break;
                case 7:
                    System.exit(0);
                    break;

                default:
                    view.displayWarningMessage("Invalid choice. Please select a valid option.");
            }

        }
    }

    private int getUserChoice() {
        String operationType = view.promoteString();
        int choice= -1;
        try {
            choice = Integer.parseInt(operationType);
        }catch (NumberFormatException e){
            view.displayErrorMessage("Invalid Input. Please enter a number.");
        }
        return choice;
    }

    private void register(){
        view.displayMessage("Enter your username");
        String username = view.promoteString();
        User registerdUser = userDao.createUser(username);
        view.displayUser(registerdUser);
    }

    private void login(){
        User user = view.promoteUserForm();
        try{
            isLoggedIn = userDao.authenticate(user);
            username = user.getUsername();
            view.displaySuccessMessage("Login successful.");
        }catch (Exception e){
            CalendarExceptionHandler.handelException(e);
        }
    }

    private void createCalendar(){
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        String calendarTitle = view.promoteCalendarForm();
        MeetingCalendar createdMeetingCalendar = calendarDao.createCalender(calendarTitle,username);
        view.displaySuccessMessage("Calendar created successfully.");
        view.displayCalendar(createdMeetingCalendar);
    }

    private void createMeeting(){
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Meeting meeting = view.promoteMeetingForm();
        Meeting createdMeeting = meetingDao.createMeeting(meeting);
        view.displaySuccessMessage("Meeting created successfully");
    }

    private boolean deleteCalendar(){
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter calendar id to delete");
        int removeId = scanner.nextInt();
        Optional<MeetingCalendar> calendar = calendarDao.findById(removeId);
        if (!calendar.isPresent()){
            view.displayErrorMessage("Calendar with id: " + removeId);
            return false;
        } else {
            calendarDao.deleteCalender(removeId);
            view.displaySuccessMessage("Calendar deleted successfully.");
            return true;
        }
    }

    public void displayCalendar(){
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter calendar id to display");
        int id = scanner.nextInt();
        Optional<MeetingCalendar> calendar = calendarDao.findById(id);
        if (!calendar.isPresent()){
            view.displayErrorMessage("No calendar found with the id: " +id);
        }else {
            view.displayCalendar(calendar.get());
        }
    }
}
