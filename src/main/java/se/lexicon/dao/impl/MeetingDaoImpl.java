package se.lexicon.dao.impl;

import se.lexicon.dao.CalendarDao;
import se.lexicon.dao.MeetingDao;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.Calendar;
import se.lexicon.model.Meeting;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MeetingDaoImpl  implements MeetingDao {
    private Connection connection;

    public MeetingDaoImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        String createQuery = "INSERT INTO meeting(title,start_time,end_time,_description,calendar_id";
        try (PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, meeting.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(meeting.getStartTime()));
            statement.setTimestamp(3, Timestamp.valueOf(meeting.getEndTime()));
            statement.setString(4, meeting.getDescription());
            statement.setInt(5, meeting.getId());
            int numberOfRowsInserted = statement.executeUpdate();
            if (numberOfRowsInserted == 0) {
                String errorMessage = "Creating meeting failed";
                throw new MySQLException(errorMessage);
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()
            ) {
                if (generatedKeys.next()) {
                    int meetingId = generatedKeys.getInt(1);
                    new Meeting(meetingId, meeting.getTitle(), meeting.getStartTime(), meeting.getEndTime(), meeting.getDescription(), meeting.getCalendar());
                    return meeting;
                } else {
                    String errorMessage = "Creating meeting failed";
                    throw new MySQLException(errorMessage);
                }
            } catch (SQLException e) {
                String errorMessage = "Error occurred while creating a meeting";
                throw new MySQLException(errorMessage, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Meeting> findById(int id) {
        String findByIdQuery = "SELECT *FROM meeting WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(findByIdQuery)) {
            statement.setInt(1,id);
            try (ResultSet resultSet= statement.executeQuery()) {
                if (resultSet.next()){
                    int Id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
                    String description = resultSet.getString("description");
                    int calendarId = resultSet.getInt("calendar_id");
                    // Retrieve the Calendar object associated with the calendarId
                    CalendarDao calendarDao = new CalendarDaoImpl(connection);
                    Optional<Calendar> calendarOpt = calendarDao.findById(calendarId);
                    if (calendarOpt.isPresent()) {
                        return Optional.of(new Meeting(Id, title, startTime, endTime, description, calendarOpt.get()));
                    }
                }
            }
        }catch (SQLException e) {
            String errorMessage = "Error occurred while finding meeting by ID: " + id;
            throw new MySQLException(errorMessage, e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Meeting> findAllMeetingsByCalenderId(int calenderId) {
        String findAllQuery = "SELECT * FROM meeting WHERE calendar_id = ?";
        List<Meeting> meetings = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(findAllQuery)) {
            statement.setString(1,String.valueOf(calenderId));
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    int Id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
                    String description = resultSet.getString("description");
                    int calendarId = resultSet.getInt("calendar_id");
                    // Retrieve the Calendar object associated with the calendarId
                    CalendarDao calendarDao = new CalendarDaoImpl(connection);
                    Optional<Calendar> calendarOpt = calendarDao.findById(calendarId);
                    if (calendarOpt.isPresent()) {
                        meetings.add(new Meeting(Id, title, startTime, endTime, description, calendarOpt.get()));
                    }
                }
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while finding Meetings by calendarId: " + calenderId;
            throw new MySQLException(errorMessage);
        }
        return meetings;
    }

    @Override
    public boolean deleteMeeting(int meetingId) {
        String deleteQuery = "DELETE FROM meeting WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)
        ){
            statement.setInt(1,meetingId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0){
                System.out.println("Delete successful");
                return true;
            }else {
                throw new MySQLException("Error deleting metting with the id: "+ meetingId);
            }
        } catch (SQLException e) {
            String errorMessage = "Error occurred while deleting meeting by ID: " + meetingId;
            throw new MySQLException(errorMessage, e);
        }
    }
}
