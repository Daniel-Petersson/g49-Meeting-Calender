package se.lexicon.dao.impl;

import com.mysql.cj.MysqlConnection;
import se.lexicon.dao.UserDao;
import se.lexicon.dao.impl.db.MeetingCalenderDBConnection;
import se.lexicon.exception.AuthenticationFailedException;
import se.lexicon.exception.MySQLException;
import se.lexicon.exception.UserExpiredException;
import se.lexicon.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {


    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User createUser(String username) {
        String createQuery = "INSERT INTO users(username, _password) VALUES(?,?)";

        try (
                PreparedStatement statement = connection.prepareStatement(createQuery);
        ) {
            User user = new User(username);
            user.newPassword();
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            int numberOfRowsInserted = statement.executeUpdate();
            if (numberOfRowsInserted == 0) {
                String errorMessage = "Creating user failed, no rows inserted.";
                throw new MySQLException(errorMessage);
            }
            return user;
        } catch (SQLException e) {
            String errorMessage = "Error occurred while creating user: " + username;
            throw new MySQLException(errorMessage);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String findByUsernameQuery = "SELECT * FROM users WHERE username = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(findByUsernameQuery)
        ) {
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String foundUsername = resultSet.getString("username");
                String foundPassword = resultSet.getString("_password");
                boolean foundExpired = resultSet.getBoolean("expired");
                User user = new User(foundUsername, foundPassword, foundExpired);
                return Optional.of(user);
            }else {
                return Optional.empty();
            }
        }catch (SQLException e){
            String errorMessage = "Error occurred while creating user: " + username;
            throw new MySQLException(errorMessage);
        }
    }

    @Override
    public boolean authenticate(User user) throws AuthenticationFailedException, UserExpiredException {
        String authenticateQuery = "SELECT * FROM users WHERE username = ? and _password = ?";
        try(
                PreparedStatement statement = connection.prepareStatement(authenticateQuery);
                ){
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                boolean isExpired = resultSet.getBoolean("expired");
                if (isExpired){
                    throw  new UserExpiredException("User is expired. username: "+user.getUsername());
                }else {
                    throw new UserExpiredException("Authentication failed. Invalid credentials.");
                }

            }
            return true;
        }catch (SQLException e) {
            throw new MySQLException("Error occurred while authentication user by username" + user.getUsername());
        }
    }
}
