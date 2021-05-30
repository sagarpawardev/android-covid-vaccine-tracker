package dev.sagar.covidvaccinetracker.facade;

import android.content.Context;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import dev.sagar.covidvaccinetracker.client.CowinApi;
import dev.sagar.covidvaccinetracker.client.CowinClient;
import dev.sagar.covidvaccinetracker.enums.FilterEnum;
import dev.sagar.covidvaccinetracker.enums.TrackerState;
import dev.sagar.covidvaccinetracker.model.CalendarByPin;
import dev.sagar.covidvaccinetracker.model.SessionCalendarEntrySchema;
import dev.sagar.covidvaccinetracker.model.Tracker;
import retrofit2.Call;
import retrofit2.Callback;

import static dev.sagar.covidvaccinetracker.Constants.localDateFormatter;
import static dev.sagar.covidvaccinetracker.enums.DoseEnum.DOSE1;
import static dev.sagar.covidvaccinetracker.enums.DoseEnum.DOSE2;
import static dev.sagar.covidvaccinetracker.enums.FilterEnum.DOSE;
import static dev.sagar.covidvaccinetracker.enums.FilterEnum.MIN_AGE;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

public class CowinFacade {

    private final StorageFacade storageFacade;
    public CowinFacade(Context context){
        this.storageFacade = new StorageFacade(context);
    }

    public void getCalendarByPin(String pinCode, Callback<CalendarByPin> callback){
        CowinApi api = CowinClient.getInstance().create(CowinApi.class);
        Call<CalendarByPin> call = api.calendarByPin(pinCode, LocalDate.now().format(localDateFormatter));
        call.enqueue(callback);
    }

    public long findAvailableCenters(CalendarByPin calendarByPin, @NonNull final Map<FilterEnum, String> filters) {

        // Number of centers that have at least 1 availability and minimum age
        return calendarByPin.getCenters().stream()
                .filter(result -> result.getSessions().stream()
                        .filter(session -> session.getMinAgeLimit()<= toInt(filters.get(MIN_AGE), 100))
                        .filter(session -> {
                            String dose = filters.get(DOSE);
                            if( equalsIgnoreCase(dose, DOSE1.toString()) ) {
                                return session.getAvailableCapacityDoes1() > 0;
                            }
                            else if( equalsIgnoreCase(dose, DOSE2.toString()) ){
                                return session.getAvailableCapacityDoes2() > 0;
                            }

                            return true;
                        })
                        .anyMatch(session -> session.getAvailableCapacity() > 0)
                ).count();
    }

    public String findLocation(CalendarByPin calendarByPin){
        Optional<SessionCalendarEntrySchema> optCenter = calendarByPin.getCenters().stream().findFirst();
        return optCenter.map(sessionCalendarEntrySchema ->
                String.format("%s, %s",sessionCalendarEntrySchema.getDistrictName(),sessionCalendarEntrySchema.getStateName())
        ).orElse(null);
    }

    public String findPincode(CalendarByPin calendarByPin){
        Optional<SessionCalendarEntrySchema> optCenter = calendarByPin.getCenters().stream().findFirst();
        return optCenter.map(SessionCalendarEntrySchema::getPincode).orElse(null);
    }

    public Tracker track(CalendarByPin calendarByPin, Map<FilterEnum, String> filters){
        String pincode = findPincode(calendarByPin);
        String location = findLocation(calendarByPin);

        if(isEmpty(pincode) || isEmpty(location)){
            return null;
        }

        Tracker tracker = new Tracker();
        tracker.setState(TrackerState.ACTIVE);
        tracker.setLocation(location);
        tracker.setPincode(pincode);
        tracker.setFilters(filters);

        this.storageFacade.add(tracker);

        return tracker;
    }

}
