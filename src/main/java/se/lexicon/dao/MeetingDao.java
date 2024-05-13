package se.lexicon.dao;

import se.lexicon.model.Meeting;

import java.util.Collection;
import java.util.Optional;

public interface MeetingDao {
    Meeting createMeeting(Meeting meeting);
    Optional<Meeting> findById(int id);
    Collection<Meeting> findAllMeetingsByCalenderId(int calenderId);
    boolean deleteMeeting(int meetingId);

    // Add methods for updating meetings  as needed..
}
