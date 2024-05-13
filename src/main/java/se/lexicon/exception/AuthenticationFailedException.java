package se.lexicon.exception;

public class AuthenticationFailedException extends Exception{
    public AuthenticationFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
