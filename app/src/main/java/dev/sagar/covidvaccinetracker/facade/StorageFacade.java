package dev.sagar.covidvaccinetracker.facade;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

import dev.sagar.covidvaccinetracker.model.Tracker;

public class StorageFacade {
    private final static String PREF_KEY = "database";
    private final Context context;
    private final Gson gson;
    public StorageFacade(Context context){
        this.context = context;
        this.gson = new Gson();
    }

    public void add(Tracker tracker){
        List<Tracker> trackers = getTrackers();

        long count = trackers.stream()
                .filter( existingTracker ->
                        existingTracker.getPincode().equals(tracker.getPincode())
                                && existingTracker.getFilters().equals(tracker.getFilters())
                ).count();
        if(count==0) {
            trackers.add(tracker);
            saveTrackers(trackers);
        }
    }

    public void update(Tracker tracker){
        List<Tracker> trackers = getTrackers();

        trackers.stream()
            .filter( existingTracker ->
                    existingTracker.getPincode().equals(tracker.getPincode())
                        && existingTracker.getFilters().equals(tracker.getFilters())
            ).forEach(
                    existingTracker -> {
                        existingTracker.setState(tracker.getState());
                        existingTracker.setLocation(tracker.getLocation());
                    }
            );

        saveTrackers(trackers);
    }

    public List<Tracker> getTrackers(){
        SharedPreferences prefs = context.getSharedPreferences("tracker_preferences", Context.MODE_PRIVATE);
        String trackerArr = prefs.getString(PREF_KEY, "[]");
        return gson.fromJson(trackerArr, new TypeToken<List<Tracker>>(){}.getType());
    }

    private void saveTrackers(List<Tracker> trackers){
        SharedPreferences prefs = context.getSharedPreferences("tracker_preferences", Context.MODE_PRIVATE);
        String result = gson.toJson(trackers);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY, result);
        editor.apply();
    }
}
