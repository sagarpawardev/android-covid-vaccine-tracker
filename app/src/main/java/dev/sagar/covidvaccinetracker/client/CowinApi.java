package dev.sagar.covidvaccinetracker.client;

import dev.sagar.covidvaccinetracker.model.CalendarByPin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CowinApi {

    @Headers({
            "Host: cdn-api.co-vin.in",
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
    })
    @GET("/api/v2/appointment/sessions/public/calendarByPin")
    Call<CalendarByPin> calendarByPin(@Query("pincode") String pincode,
                                      @Query("date") String date);

}
