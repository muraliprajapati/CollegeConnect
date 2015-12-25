package com.sophomoreventure.collegeconnect;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Murali on 24/12/2015.
 */
public class EventUtility {
    public static String getFriendlyDayString(long dateInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateInMillis);
        if (calendar.equals(new GregorianCalendar(1970, 0, 1))) {
            return "Notification";
        }
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a");
        return shortenedDateFormat.format(dateInMillis);
    }

    public static boolean isEmpty(Calendar[] array) {
        boolean empty = false;
        for (Calendar anArray : array) {
            empty = anArray == null;
        }
        return empty;
    }
}

