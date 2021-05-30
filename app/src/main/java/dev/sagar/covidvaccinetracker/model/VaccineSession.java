package dev.sagar.covidvaccinetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VaccineSession {
    @JsonProperty("session_id")
    private UUID sessionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty("available_capacity")
    private int availableCapacity;

    @JsonProperty("available_capacity_dose1")
    private int availableCapacityDoes1;

    @JsonProperty("available_capacity_dose2")
    private int availableCapacityDoes2;

    @JsonProperty("min_age_limit")
    private int minAgeLimit;

    private String vaccine;

    private List<String> slots;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public int getAvailableCapacityDoes1() {
        return availableCapacityDoes1;
    }

    public void setAvailableCapacityDoes1(int availableCapacityDoes1) {
        this.availableCapacityDoes1 = availableCapacityDoes1;
    }

    public int getAvailableCapacityDoes2() {
        return availableCapacityDoes2;
    }

    public void setAvailableCapacityDoes2(int availableCapacityDoes2) {
        this.availableCapacityDoes2 = availableCapacityDoes2;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(int minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}
