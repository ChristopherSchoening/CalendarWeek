package de.christopherschoening.calendarweek;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DEBUG_TAG = MainActivity.class.getName();

    private static AlarmReceiver mAlarm;

    private TextView mCalendarWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);    //set transition
        setContentView(R.layout.activity_main);

        //display current calendar week
        mCalendarWeek = findViewById(R.id.calendar_week);
        setCalendarWeek();

        //shared preferences
        setupSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_action_icon) {
            Intent startAboutIntent = new Intent(this, AboutActivity.class);
            startActivity(startAboutIntent);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void setCalendarWeek() {
        int calendarWeek = TimeUtils.getCalendarWeek();
        mCalendarWeek.setText("" + calendarWeek);
    }

    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("show_notification_checkbox")) {
            Log.d(DEBUG_TAG, "onSharedPreferenceChanged : " + key);
            // return state of checkbox and perform logic to create or delete notification
            adjustNotificationStateToPreferences(sharedPreferences.getBoolean(key, false));
        }
    }

    protected void adjustNotificationStateToPreferences(boolean show_notification) {
        //remove notifications
        NotificationUtils.removeNotifications(this);

        //create a notification if prefered
        if (show_notification) {
            //create notification
            NotificationUtils.createNotificationChannel(this);
            NotificationUtils.createNotification(this);
            //set alarm to update every week
            createAlarm();
        }else{
            //changed to false from true, so alarm needs to be canceled
            if(mAlarm != null) {    // if there wasnt an alarm set you cannot call cancelAlarm
                mAlarm.cancelAlarm(this);
            }
        }
    }

    private void createAlarm(){
        mAlarm = new AlarmReceiver();
        mAlarm.setAlarm(this);
    }


    public void rateApp(View view) {
        Context context = this;
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        private static final String DEBUG_TAG = MainActivity.SettingsFragment.class.getName();
        public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

    }

}
