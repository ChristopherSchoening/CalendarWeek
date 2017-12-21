package de.christopherschoening.calendarweek;

import android.util.Log;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by christopher on 19.12.17.
 */

public final class TimeUtils {

    private static final String DEBUG_TAG = TimeUtils.class.getName();

    public static int getCalendarWeek(){
        Calendar calendar = new GregorianCalendar();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        Log.d(DEBUG_TAG, "getCalendarWeek: currentWeak : "+currentWeek);
        return currentWeek;
    }
    
}
