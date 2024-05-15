package se.lexicon.model;

import java.util.ArrayList;
import java.util.List;

public class MeetingCalendar {
    private int id;
    private String title;
    private String username;
    private List<Meeting> meetings;

    //Constructors

    //Constructor with minimum required parameters
    public MeetingCalendar(String title, String username) {
        this.title = title;
        this.username = username;
        this.meetings = new ArrayList<>();
    }

    public MeetingCalendar(int id, String title, String username) {
        this.id = id;
        this.title = title;
        this.username = username;
    }



    public MeetingCalendar(String title) {
        this.title = title;
    }

    //Constructor for DB Query
    public MeetingCalendar(int id, String title, String username, List<Meeting> meetings) {
        this(title, username); // Call the other constructor
        this.id = id;
        this.meetings = meetings;
    }

    //Constructor for DB createQuery
    public MeetingCalendar(String title, String username, List<Meeting> meetings) {
        this(title, username); // Call the other constructor
        this.meetings = meetings;
    }

    //Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }
    //Methods

    public void addMeeting(Meeting meeting){
        if (meetings == null)meetings=new ArrayList<>();
        meetings.add(meeting);
    }
    public void removeMeeting(Meeting meeting){
        if (meetings==null)throw new IllegalArgumentException("Meeting list is null");
        if (meeting==null)throw new IllegalArgumentException("Meeting parameters is null");
        meetings.remove(meeting);
    }

    public String calendarInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Calendar Info:").append("\n");
        stringBuilder.append("Id ").append(id).append("\n");
        stringBuilder.append("Title ").append(title).append("\n");
        stringBuilder.append("Username ").append(username).append("\n");
        return stringBuilder.toString();
    }
}
