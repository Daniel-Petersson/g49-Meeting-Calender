package se.lexicon.dao;

import jdk.jshell.spi.ExecutionControl;
import se.lexicon.exception.AuthenticationFailedException;
import se.lexicon.exception.UserExpiredException;
import se.lexicon.model.User;

import javax.naming.AuthenticationException;
import java.util.Optional;

public interface UserDao {
    User createUser(String username);

    Optional<User> findByUsername(String username);

    boolean authenticate(User user) throws AuthenticationFailedException, UserExpiredException;

    //Add more methods according to project later on.
}
