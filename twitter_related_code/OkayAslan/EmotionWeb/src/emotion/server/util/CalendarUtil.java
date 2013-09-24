package emotion.server.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil
{
     public Calendar converDateToCalendar (Date date)
     {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date); 
        return cal;
     }
}
