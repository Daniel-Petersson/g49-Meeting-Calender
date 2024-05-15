package se.lexicon;

import com.mysql.cj.MysqlConnection;
import se.lexicon.controller.CalendarController;
import se.lexicon.dao.CalendarDao;
import se.lexicon.dao.MeetingDao;
import se.lexicon.dao.UserDao;
import se.lexicon.dao.impl.CalendarDaoImpl;
import se.lexicon.dao.impl.MeetingDaoImpl;
import se.lexicon.dao.impl.UserDaoImpl;
import se.lexicon.dao.impl.db.MeetingCalenderDBConnection;
import se.lexicon.model.Meeting;
import se.lexicon.view.CalendarConsoleUI;
import se.lexicon.view.CalendarView;

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
        CalendarView view = new CalendarConsoleUI();
        UserDao userdao = new UserDaoImpl(connection);
        CalendarDao calendarDao = new CalendarDaoImpl(connection);
        MeetingDao meetingDao = new MeetingDaoImpl(connection);
        CalendarController controller = new CalendarController(view,userdao,calendarDao,meetingDao);
        controller.run();

    }
}
