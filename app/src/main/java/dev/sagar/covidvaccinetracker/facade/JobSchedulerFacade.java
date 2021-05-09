package dev.sagar.covidvaccinetracker.facade;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dev.sagar.covidvaccinetracker.receiver.AlarmReceiver;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_NO_CREATE;
import static android.content.Context.ALARM_SERVICE;
import static dev.sagar.covidvaccinetracker.Constants.POLL_INTERVAL;

public class JobSchedulerFacade {
    private final Context context;
    public JobSchedulerFacade(Context context){
        this.context = context;
    }

    public void schedule(int requestCode) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        manager.setRepeating(RTC_WAKEUP, System.currentTimeMillis(), POLL_INTERVAL, pendingIntent);
    }

    public void cancel(int requestCode) {
        try {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, FLAG_NO_CREATE);

            if (pendingIntent == null) {
                return;
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

            pendingIntent.cancel();
        } catch (Exception e) {
            Log.e("Covid Tracker", "Failed While cancelling alarm", e); // we don't care if it fails, we don't want to crash the library
        }
    }
}
