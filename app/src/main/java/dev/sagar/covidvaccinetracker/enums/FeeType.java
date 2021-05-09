package dev.sagar.covidvaccinetracker.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FeeType {
    FREE(1, "Free"),
    PAID(2, "Paid");

    private Integer id;
    private String name;

    FeeType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
