package dev.sagar.covidvaccinetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarByPin {
    private List<SessionCalendarEntrySchema> centers;

    public List<SessionCalendarEntrySchema> getCenters() {
        return centers;
    }

    public void setCenters(List<SessionCalendarEntrySchema> centers) {
        this.centers = centers;
    }
}
