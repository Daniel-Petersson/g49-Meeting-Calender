package se.lexicon.exception;

import java.sql.SQLException;

public class DBConnectionException extends RuntimeException{
    public DBConnectionException(String message, SQLException e) {
        super(message);
    }
}
