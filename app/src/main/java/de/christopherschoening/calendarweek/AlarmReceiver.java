package de.christopherschoening.calendarweek;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by christopher on 20.12.17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = AlarmReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // update notifications
       updateNotifications(context);
    }

    //updates the notification, duh
    private void updateNotifications(Context context) {
        //check if notification actually shown
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notification_shown = sharedPreferences.getBoolean("show_notification_checkbox", false);
        if(notification_shown){
            //update notification (delete old & create new)
            NotificationUtils.removeNotifications(context);
            NotificationUtils.createNotification(context);
        }
    }

    //sets an alarm for the notification update
    public void setAlarm(Context context)
    {
        // set time for alarm
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //create alarm manager
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmReceiver.class);
        i.setAction("de.christopherschoening.calendarweek.START_ALARM");

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pi); //every 7 days
    }

    //should check if alarm was set before firing this method
    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
