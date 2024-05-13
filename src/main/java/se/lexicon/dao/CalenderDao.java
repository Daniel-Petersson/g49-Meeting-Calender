package se.lexicon.dao;

import se.lexicon.model.Calender;

import java.util.Collection;
import java.util.Optional;

public interface CalenderDao {
    Calender createCalender(String title, String username);
    Optional<Calender> findById(int id);
    Collection<Calender> findByUsername(String username);
    Optional<Calender> findByTitle(String title);
    boolean deleteCalender(int id);
}
