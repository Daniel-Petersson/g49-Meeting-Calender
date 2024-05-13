package se.lexicon;

import se.lexicon.dao.impl.db.MeetingCalenderDBConnection;

import java.sql.Connection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Connection connection = MeetingCalenderDBConnection.getConnection();
        System.out.println( "Hello World!" );
    }
}
