package dev.sagar.covidvaccinetracker.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalTime;

import dev.sagar.covidvaccinetracker.serializer.LocalDateDeserializer;
import dev.sagar.covidvaccinetracker.serializer.LocalDateSerializer;
import dev.sagar.covidvaccinetracker.serializer.LocalTimeDeserializer;
import dev.sagar.covidvaccinetracker.serializer.LocalTimeSerializer;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CowinClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://cdn-api.co-vin.in";

    public static Retrofit getInstance() {
        if (retrofit == null) {

            ObjectMapper mapper = new ObjectMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
            mapper.registerModule(javaTimeModule);

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .build();
        }
        return retrofit;
    }

}
