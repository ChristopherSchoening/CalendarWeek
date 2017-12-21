package de.christopherschoening.calendarweek;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by christopher on 20.12.17.
 */

public class NotificationUtils {

    private static final String DEBUG_TAG = NotificationUtils.class.getName();

    private static final String NOTIFICATION_CHANNEL_ID = "my_id";
    private static final int NOTIFICATION_CHANNEL_ID_INT = 555;


    public static void createNotificationChannel(Context context) {
        // create notification channel
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel_label),
                    NotificationManager.IMPORTANCE_HIGH
            );
            mChannel.enableLights(false);
            mChannel.enableVibration(false);

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public static void createNotification(Context context) {
        //create notification

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(getSmallIcon(context))
                        .setContentTitle(context.getString(R.string.notification_channel_label))
                        .setContentText(context.getString(R.string.current_weak) + " " + TimeUtils.getCalendarWeek())
                        .setOngoing(true)   //makes it resist "clear all notifications" action / persistent
                        .setShowWhen(false);


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)

            stackBuilder.addParentStack(MainActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack if there wasn't an intent before

            stackBuilder.addNextIntent(resultIntent);


            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(NOTIFICATION_CHANNEL_ID_INT, mBuilder.build());


    }

    public static void removeNotifications(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    /**
     * Helper function.
     * Returns icon for given week.
     *
     * @return int
     */
    private static int getSmallIcon(Context context) {
        int week = TimeUtils.getCalendarWeek();

        int icon = context.getResources().getIdentifier("ic_" + week, "drawable", context.getPackageName());

        if (icon == 0) {  // icon == 0 if no resource found
            icon = 1;
        }

        return icon;
    }
}