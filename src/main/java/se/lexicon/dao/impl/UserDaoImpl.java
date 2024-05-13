package se.lexicon.dao.impl;

import se.lexicon.dao.UserDao;
import se.lexicon.exception.AuthenticationFailedException;
import se.lexicon.exception.UserExpiredException;
import se.lexicon.model.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public User createUser(String username) {
        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public boolean authenticate(User user) throws AuthenticationFailedException, UserExpiredException {
        return false;
    }
}
