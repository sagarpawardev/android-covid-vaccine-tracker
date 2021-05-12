package dev.sagar.covidvaccinetracker.facade;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class LoggerFacade {

    private final static String PREF_KEY = "database";
    private final Context context;
    private final Gson gson;

    public LoggerFacade(Context context){
        this.context = context;
        this.gson = new Gson();
    }

    public void log(String log){
        List<String> logs = getLogs();
        logs.add(0, log);
        int trimSize = Math.min(24*12, logs.size());
        logs = logs.subList(0, trimSize );
        saveLogs(logs);
    }

    public void saveLogs(List<String> logs){
        SharedPreferences prefs = context.getSharedPreferences("logger_preference", Context.MODE_PRIVATE);
        String result = gson.toJson(logs);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY, result);
        editor.apply();
    }

    public List<String> getLogs(){
        SharedPreferences prefs = context.getSharedPreferences("logger_preference", Context.MODE_PRIVATE);
        String trackerArr = prefs.getString(PREF_KEY, "[]");
        return gson.fromJson(trackerArr, new TypeToken<List<String>>(){}.getType());
    }
}
