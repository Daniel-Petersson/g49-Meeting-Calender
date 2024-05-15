package se.lexicon.exception;

public class UserExpiredException extends Exception{
    public UserExpiredException(String message) {
        super(message);
    }
}
