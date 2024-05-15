package se.lexicon.dao.impl;


import se.lexicon.dao.MeetingDao;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.MeetingCalendar;
import se.lexicon.model.Meeting;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MeetingDaoImpl  implements MeetingDao {
    private final Connection connection;

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
    String selectQuery = "SELECT m.*, mc.username as username, mc.title as calendarTitle FROM meeting m inner join meeting_calendars mc on m.calendar_id = mc.id WHERE m.id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Optional<Meeting> meeting = convertToMeeting(resultSet);
            if (meeting.isPresent()) {
                return meeting;
            }
        }
    } catch (SQLException e) {
        String errorMessage = "Error occurred while finding a meeting by ID " + id;
        throw new MySQLException(errorMessage, e);
    }
    return Optional.empty();
}

@Override
public Collection<Meeting> findAllMeetingsByCalenderId(int calenderId) {
    String findAllQuery = "SELECT m.*, mc.username as username, mc.title as calendarTitle FROM meeting m inner join meeting_calendars mc on m.calendar_id = mc.id WHERE m.calendar_id = ?";
    List<Meeting> meetings = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(findAllQuery)) {
        statement.setInt(1, calenderId);
        try (ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                Optional<Meeting> meeting = convertToMeeting(resultSet);
                meeting.ifPresent(meetings::add);
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
                throw new MySQLException("Error deleting meeting with the id: "+ meetingId);
            }
        } catch (SQLException e) {
            String errorMessage = "Error occurred while deleting meeting by ID: " + meetingId;
            throw new MySQLException(errorMessage, e);
        }
    }

    private static Optional<Meeting> convertToMeeting(ResultSet resultSet) throws SQLException {
        int meetingId = resultSet.getInt("id");
        String title = resultSet.getString("title");
        Timestamp startTime = resultSet.getTimestamp("start_time");
        Timestamp endTime = resultSet.getTimestamp("end_time");
        String description = resultSet.getString("description");
        int calendarId = resultSet.getInt("calendar_id");
        String calendarUsername = resultSet.getString("username");
        String calendarTitle = resultSet.getString("calendarTitle");

        LocalDateTime startDateTime = startTime.toLocalDateTime();
        LocalDateTime endDateTime = endTime.toLocalDateTime();

        return Optional.of(new Meeting(meetingId, title, startDateTime, endDateTime, description, new MeetingCalendar(calendarId, calendarUsername, calendarTitle)));
    }
}
