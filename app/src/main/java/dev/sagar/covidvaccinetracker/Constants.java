package dev.sagar.covidvaccinetracker;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final int POLL_INTERVAL = 5*60*1000; //5 minutes
    public static final String PINCODE = "pincode";
    public static final String COWIN_PORTAL = "https://www.cowin.gov.in/home";
    public static final String COWIN_LOGIN = "https://selfregistration.cowin.gov.in/";
    public static final int ALARM_PROCESS_ID = 123456;
}
