package se.lexicon.dao.impl;

import se.lexicon.dao.CalendarDao;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.Calendar;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CalendarDaoImpl implements CalendarDao {

    private Connection connection;

    public CalendarDaoImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Calendar createCalender(String title, String username) {
        String createQuery = "INSERT INTO calendars(title, username) VALUES(?,?)";
        try(PreparedStatement statement = connection.prepareStatement(createQuery)
        ){
            Calendar calendar = new Calendar(title,username);
            statement.setString(1, calendar.getTitle());
            statement.setString(2,calendar.getUsername());
            int numberOfRowsInserted = statement.executeUpdate();
            if (numberOfRowsInserted == 0){
                String errorMessage = "Creating calendar failed";
                throw new MySQLException(errorMessage);
            }
            return calendar;
        }catch (SQLException e) {
            String errorMessage = "Error occured while creating calendar";
            throw new MySQLException(errorMessage);
        }
    }

    @Override
    public Optional<Calendar> findById(int id) {
        String findByIdQuery = "SELECT * FROM calendars WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(findByIdQuery)
        ){
            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()
            ){
                if (resultSet.next()){
                    String username = resultSet.getString("username");
                    String title = resultSet.getString("title");
                    return Optional.of(new Calendar(id,username,title));
                }
            }

        }catch (SQLException e){
            String errorMessage = "Error occurred while finding Calendars by username: " + id;
            throw new MySQLException(errorMessage);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Calendar> findByUsername(String username) {
        String findByUsernameQuery = "SELECT * FROM calendars WHERE username = ?";
        List<Calendar> calendars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(findByUsernameQuery)
        ){
            statement.setString(1,username);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    calendars.add(new Calendar(id,title,username));
                }
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while finding Calendars by username: " + username;
            throw new MySQLException(errorMessage);
        }
        return calendars;
    }

    @Override
    public Optional<Calendar> findByTitle(String title) {
        String findByTitleQuery = "SELECT * FROM calendars WHERE title = ?";
        try(PreparedStatement statement = connection.prepareStatement(findByTitleQuery)
        ){
            statement.setString(1,title);
            try (ResultSet resultSet = statement.executeQuery()
            ){
                if (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    return Optional.of(new Calendar(id,username,title));
                }
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while finding Calendars by title: " + title;
            throw new MySQLException(errorMessage);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCalender(int id) {
        return false;
    }
}
