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
                String errorMessage = "Creating user failed";
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
        //step1: define a select query
        String query = "SELECT * FROM users WHERE username = ? and _password = ?";
        //step2: prepared statement
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

            //step3: set parameters to prepared statement
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            //step4: execute query
            ResultSet resultSet = preparedStatement.executeQuery();
            //step5: check the result set
            if (resultSet.next()) {
                //step6: if result set exists
                //step7: check if user expired -> throw exception
                boolean isExpired = resultSet.getBoolean("expired");
                if (isExpired) {
                    throw new UserExpiredException("User is expired. username: " + user.getUsername());
                }
            } else { //step8: else if the result set was null -> throw exception
                throw new AuthenticationFailedException("Authentication failed. Invalid credentials.");
            }
            //step9: return true
            return true;
        } catch (SQLException e) {
            throw new MySQLException("Error occured while authenticationg user by username" + user.getUsername());
        }

    }
}
