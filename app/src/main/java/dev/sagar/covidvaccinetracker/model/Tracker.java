package dev.sagar.covidvaccinetracker.model;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Map;

import dev.sagar.covidvaccinetracker.enums.FilterEnum;
import dev.sagar.covidvaccinetracker.enums.TrackerState;

public class Tracker {
    private String pincode;
    private String location;
    private TrackerState state;

    @NonNull
    private Map<FilterEnum, String> filters = Collections.emptyMap();

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TrackerState getState() {
        return state;
    }

    public void setState(TrackerState state) {
        this.state = state;
    }

    @NonNull
    public Map<FilterEnum, String> getFilters() {
        return filters;
    }

    public void setFilters(@NonNull Map<FilterEnum, String> filters) {
        this.filters = filters;
    }
}
