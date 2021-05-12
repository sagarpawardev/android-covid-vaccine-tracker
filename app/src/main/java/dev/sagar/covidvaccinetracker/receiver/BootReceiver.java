package dev.sagar.covidvaccinetracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dev.sagar.covidvaccinetracker.facade.JobSchedulerFacade;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static dev.sagar.covidvaccinetracker.Constants.ALARM_PROCESS_ID;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG="BootReceiver";

    @Override public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
            JobSchedulerFacade jobFacade = new JobSchedulerFacade(context);
            jobFacade.schedule(ALARM_PROCESS_ID);
        }
    }
}
