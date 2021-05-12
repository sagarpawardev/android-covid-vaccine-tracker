package dev.sagar.covidvaccinetracker.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.time.LocalDateTime;

import dev.sagar.covidvaccinetracker.R;
import dev.sagar.covidvaccinetracker.facade.CowinFacade;
import dev.sagar.covidvaccinetracker.facade.LoggerFacade;
import dev.sagar.covidvaccinetracker.facade.StorageFacade;
import dev.sagar.covidvaccinetracker.model.CalendarByPin;
import dev.sagar.covidvaccinetracker.model.Tracker;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dev.sagar.covidvaccinetracker.Constants.COWIN_LOGIN;
import static dev.sagar.covidvaccinetracker.enums.TrackerState.ACTIVE;
import static java.lang.String.format;

public class AlarmReceiver extends BroadcastReceiver {
    private CowinFacade cowinFacade;
    private LoggerFacade loggerFacade;

    @Override
    public void onReceive(Context context, Intent intent) {
        cowinFacade = new CowinFacade(context);
        loggerFacade = new LoggerFacade(context);
        StorageFacade storageFacade = new StorageFacade(context);

        storageFacade.getTrackers().stream()
                .filter(tracker -> tracker.getState().equals(ACTIVE))
                .forEach(tracker -> cowinFacade.getCalendarByPin(tracker.getPincode(), trackCallback(context, tracker))
                );
    }

    private Callback<CalendarByPin> trackCallback(Context context, Tracker tracker) {
        return new Callback<CalendarByPin>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CalendarByPin> call, Response<CalendarByPin> response) {
                CalendarByPin calendarByPin = response.body();
                long count = cowinFacade.findAvailableCenters(calendarByPin, tracker.getFilters());
                //Toast.makeText(context, "Called API for "+tracker.getPincode(), Toast.LENGTH_LONG).show(); //This is only for testing
                if (count > 0) {
                    addNotification(context, tracker, count);
                }

                // Log Success
                String log = String.format("Check success for %s at %s found availability: %s", tracker.getPincode(), LocalDateTime.now(), count);
                loggerFacade.log(log);
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CalendarByPin> call, Throwable t) {
                // Log Fail
                String log = String.format("Check failed for %s at %s", tracker.getPincode(), LocalDateTime.now());
                loggerFacade.log(log);
            }
        };
    }

    private void addNotification(Context context, Tracker tracker, long availableCount) {
        Intent cowinIntent = new Intent(Intent.ACTION_VIEW);
        cowinIntent.setData(Uri.parse(COWIN_LOGIN));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, cowinIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Sets an ID for the notification, so it can be updated.
        int notifyID = Integer.parseInt(tracker.getPincode());
        String CHANNEL_ID = tracker.getPincode();
        CharSequence name = tracker.getLocation();
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        Notification notification = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(format("Availability at %s", tracker.getPincode()))
                .setContentText(format("%s available at %s within 7 days", availableCount, tracker.getLocation()))
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);

        // Issue the notification
        mNotificationManager.notify(notifyID, notification);
    }
}
