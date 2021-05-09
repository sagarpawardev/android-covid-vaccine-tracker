package dev.sagar.covidvaccinetracker.facade;

import android.content.Context;

import java.util.List;

import dev.sagar.covidvaccinetracker.model.Tracker;

import static dev.sagar.covidvaccinetracker.enums.TrackerState.ACTIVE;
import static dev.sagar.covidvaccinetracker.enums.TrackerState.PAUSED;

public class TrackerFacade {

    private final StorageFacade storageFacade;
    public TrackerFacade(Context context){
        storageFacade = new StorageFacade(context);
    }

    public void pause(Tracker tracker){
        tracker.setState(PAUSED);
        storageFacade.update(tracker);
    }

    public void start(Tracker tracker){
        tracker.setState(ACTIVE);
        storageFacade.update(tracker);
    }

    public List<Tracker> getAllTrackers(){
        return storageFacade.getTrackers();
    }
}
