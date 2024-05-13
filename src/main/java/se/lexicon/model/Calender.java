package se.lexicon.model;

import java.util.ArrayList;
import java.util.List;

public class Calender {
    private int id;
    private String title;
    private String username;
    private List<Meeting> meetings;

    //Constructors

    //todo chain constructors

    public Calender(String title, String username) {
        this.title = title;
        this.username = username;
    }

    //DB query

    public Calender(int id, String title, String username, List<Meeting> meetings) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.meetings = meetings;
    }

    public Calender(String title) {
        this.title = title;
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

    public String calanderInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Calendar Info:").append("\n");
        stringBuilder.append("Id ").append(id).append("\n");
        stringBuilder.append("Title ").append(title).append("\n");
        stringBuilder.append("Username ").append(username).append("\n");
        return stringBuilder.toString();
    }
}
