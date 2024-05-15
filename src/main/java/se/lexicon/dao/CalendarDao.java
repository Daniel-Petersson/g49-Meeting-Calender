package se.lexicon.dao;

import se.lexicon.model.MeetingCalendar;

import java.util.Collection;
import java.util.Optional;

public interface CalendarDao {
    MeetingCalendar createCalender(String title, String username);
    Optional<MeetingCalendar> findById(int id);
    Collection<MeetingCalendar> findByUsername(String username);
    Optional<MeetingCalendar> findByTitle(String title);
    boolean deleteCalender(int id);
}
