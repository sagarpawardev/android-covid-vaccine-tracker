package dev.sagar.covidvaccinetracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import dev.sagar.covidvaccinetracker.R;
import dev.sagar.covidvaccinetracker.facade.LoggerFacade;

public class LogListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list);

        ListView lvLogs = findViewById(R.id.lvLogs);

        List<String> logs = new LoggerFacade(this).getLogs();
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.item_log_list, R.id.tvLog, logs);
        lvLogs.setAdapter(adapter);
    }
}