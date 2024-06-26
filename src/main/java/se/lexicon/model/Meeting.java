package se.lexicon.model;

import java.time.LocalDateTime;

public class Meeting {
    private int id;
    private String title;
    private LocalDateTime startTime; // 2020-01-01 10:00
    private LocalDateTime endTime; // 2019-01-01 12:00
    private String description;
    private MeetingCalendar meetingCalendar;

    //Constructors
    //Create meeting

    //Constructor with minimum required parameters
    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.timeValidation();
    }

    //Constructor for DB Query
    public Meeting(int id, String title, LocalDateTime startTime, LocalDateTime endTime, String description, MeetingCalendar meetingCalendar) {
        this(title, startTime, endTime, description); // Call the other constructor
        this.id = id;
        this.meetingCalendar = meetingCalendar;
    }

    //Constructor for DB createQuery
    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, String description, MeetingCalendar meetingCalendar) {
        this(title, startTime, endTime, description); // Call the other constructor
        this.meetingCalendar = meetingCalendar;
    }

    //Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMeetingCalendar(MeetingCalendar meetingCalendar) {
        this.meetingCalendar = meetingCalendar;
    }

//Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public MeetingCalendar getCalendar() {
        return meetingCalendar;
    }

    //Methods
    public String meetingInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Meeting Info:").append("\n");
        stringBuilder.append("Id ").append(id).append("\n");
        stringBuilder.append("Title ").append(title).append("\n");
        stringBuilder.append("StartTime ").append(startTime).append("\n");
        stringBuilder.append("EndTime ").append(endTime).append("\n");
        stringBuilder.append("Description ").append(description).append("\n");
        stringBuilder.append("Calendar ").append((meetingCalendar != null ) ? meetingCalendar.calendarInfo() : " - ").append("\n");
        return stringBuilder.toString();
    }

    private void timeValidation(){
        if (this.endTime.isBefore(this.startTime)) throw new IllegalArgumentException("End time must be after the start time");
        if (this.startTime.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Start time must be in the future");
    }


}
