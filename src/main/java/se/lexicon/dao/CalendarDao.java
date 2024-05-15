package se.lexicon.dao;

import se.lexicon.model.Calendar;

import java.util.Collection;
import java.util.Optional;

public interface CalendarDao {
    Calendar createCalender(String title, String username);
    Optional<Calendar> findById(int id);
    Collection<Calendar> findByUsername(String username);
    Optional<Calendar> findByTitle(String title);
    boolean deleteCalender(int id);
}
