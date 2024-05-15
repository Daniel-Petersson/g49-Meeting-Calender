package se.lexicon.dao.impl;

import se.lexicon.dao.CalendarDao;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.MeetingCalendar;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CalendarDaoImpl implements CalendarDao {

    private final  Connection connection;

    public CalendarDaoImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public MeetingCalendar createCalender(String title, String username) {
        String insertQuery = "INSERT INTO meeting_calendars (username, title) VALUES (?, ?)";

        try (
                PreparedStatement statement = connection.prepareStatement(insertQuery,PreparedStatement.RETURN_GENERATED_KEYS) ) {

            statement.setString(1, username);
            statement.setString(2, title);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                String errorMessage = "Creating calendar failed, no rows affected.";
                throw new MySQLException(errorMessage);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int calendarId = generatedKeys.getInt(1);
                    return new MeetingCalendar(calendarId, username, title);
                } else {
                    String errorMessage = "Creating calendar failed, no ID obtained.";
                    throw new MySQLException(errorMessage);
                }
            }

        } catch (SQLException e) {
            String errorMessage = "An error occurred while creating a calendar.";
            throw new MySQLException(errorMessage, e);
        }
    }

    @Override
    public Optional<MeetingCalendar> findById(int id) {
        String findByIdQuery = "SELECT * FROM meeting_calendars WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(findByIdQuery)
        ){
            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()
            ){
                if (resultSet.next()){
                    String username = resultSet.getString("username");
                    String title = resultSet.getString("title");
                    return Optional.of(new MeetingCalendar(id,username,title));
                }
            }

        }catch (SQLException e){
            String errorMessage = "Error occurred while finding calendars by id: " + id;
            throw new MySQLException(errorMessage);
        }
        return Optional.empty();
    }

    @Override
    public Collection<MeetingCalendar> findByUsername(String username) {
        String findByUsernameQuery = "SELECT * FROM meeting_calendars WHERE username = ?";
        List<MeetingCalendar> meetingCalendars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(findByUsernameQuery)
        ){
            statement.setString(1,username);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    meetingCalendars.add(new MeetingCalendar(id,title,username));
                }
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while finding meeting_calendars by username: " + username;
            throw new MySQLException(errorMessage);
        }
        return meetingCalendars;
    }

    @Override
    public Optional<MeetingCalendar> findByTitle(String title) {
        String findByTitleQuery = "SELECT * FROM meeting_calendars WHERE title = ?";
        try(PreparedStatement statement = connection.prepareStatement(findByTitleQuery)
        ){
            statement.setString(1,title);
            try (ResultSet resultSet = statement.executeQuery()
            ){
                if (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    return Optional.of(new MeetingCalendar(id,username,title));
                }
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while finding meeting_calendars by title: " + title;
            throw new MySQLException(errorMessage);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCalender(int id) {
        String deleteQuery = "DELETE FROM meeting_calendars WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)
        ){
            statement.setInt(1,id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0){
                System.out.println("Delete successful");
                return true;
            }else {
                throw new MySQLException("Error deleting meeting with the id: "+ id);
            }
        } catch (SQLException e) {
            String errorMessage = "Error occurred while deleting meeting by ID: " + id;
            throw new MySQLException(errorMessage, e);
        }
    }
}
