package dev.sagar.covidvaccinetracker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.sagar.covidvaccinetracker.Constants;
import dev.sagar.covidvaccinetracker.R;
import dev.sagar.covidvaccinetracker.enums.FilterEnum;
import dev.sagar.covidvaccinetracker.facade.CowinFacade;
import dev.sagar.covidvaccinetracker.facade.JobSchedulerFacade;
import dev.sagar.covidvaccinetracker.facade.TrackerFacade;
import dev.sagar.covidvaccinetracker.model.CalendarByPin;
import dev.sagar.covidvaccinetracker.model.Tracker;
import dev.sagar.covidvaccinetracker.receiver.AlarmReceiver;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.TRANSPARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static dev.sagar.covidvaccinetracker.Constants.COWIN_PORTAL;
import static dev.sagar.covidvaccinetracker.enums.FilterEnum.MIN_AGE;

public class MainActivity extends AppCompatActivity {

    private CowinFacade cowinFacade;
    private JobSchedulerFacade jobFacade;
    private TrackerFacade trackerFacade;
    private Button btnTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cowinFacade = new CowinFacade(this);
        jobFacade = new JobSchedulerFacade(this);
        trackerFacade = new TrackerFacade(this);
        final RadioGroup rgMinAge = findViewById(R.id.rgMinAge);
        final EditText etPincode = findViewById(R.id.etPincode);
        btnTrack = findViewById(R.id.btnTrack);

        btnTrack.setOnClickListener(view -> {
            String pincode = etPincode.getText().toString();

            //Extract filters
            Map<FilterEnum, String> filters = new HashMap<>();
            int minAge = rgMinAge.getCheckedRadioButtonId()==R.id.rb18Plus ? 18: 45;
            filters.put(MIN_AGE, String.valueOf(minAge));

            cowinFacade.getCalendarByPin(pincode, pincodeCallback(filters));
            btnTrack.setText(R.string.checking);
            btnTrack.setEnabled(false);
        });

        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnTrack.setEnabled(charSequence.length() == 6);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        refreshTrackers();

        // Start Alarm
        startAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if(item.getItemId() == R.id.menuLogs){
            Intent intent = new Intent(this, LogListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAlarm() {
        this.jobFacade.schedule(123456);
    }

    public void cancelAlarm() {
        this.jobFacade.cancel(123456);
    }

    public Callback<CalendarByPin> pincodeCallback(Map<FilterEnum, String> filters) {
        return new Callback<CalendarByPin>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CalendarByPin> call, Response<CalendarByPin> response) {
                CalendarByPin calendarByPin = response.body();
                if (calendarByPin != null) {
                    // Find Available Centers
                    TextView tvAvailableCenters = findViewById(R.id.tvAvailableCenters);
                    long availableCenters = cowinFacade.findAvailableCenters(calendarByPin, filters);
                    String location = cowinFacade.findLocation(calendarByPin);
                    tvAvailableCenters.setText(String.format("Currently %s centers available in %s", availableCenters, location));

                    // Add to tracker list
                    Tracker tracker = cowinFacade.track(calendarByPin, filters);
                    refreshTrackers();
                } else {
                    Toast.makeText(MainActivity.this, "Error while calling API", Toast.LENGTH_SHORT).show();
                }

                btnTrack.setEnabled(true);
                btnTrack.setText(R.string.track);
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CalendarByPin> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Unable to call API", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void addTracker(Tracker tracker) {
        if (tracker == null) {
            Toast.makeText(this, "Can't add tracker", Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayout llTrackerList = findViewById(R.id.llTrackerList);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View viewTrackerItem = inflater.inflate(R.layout.item_tracker, llTrackerList, false);
        TextView tvDistrict = viewTrackerItem.findViewById(R.id.tvDistrict);
        TextView tvPincode = viewTrackerItem.findViewById(R.id.tvPincode);
        TextView tvState = viewTrackerItem.findViewById(R.id.tvTrackerState);
        TextView tvMinAge = viewTrackerItem.findViewById(R.id.tvMinAge);
        tvDistrict.setText(tracker.getLocation());
        tvPincode.setText(tracker.getPincode());
        tvState.setText(WordUtils.capitalize(tracker.getState().toString().toLowerCase()));
        tvMinAge.setText( String.format("Age %s+", tracker.getFilters().getOrDefault(MIN_AGE, "45")) );

        viewTrackerItem.setOnClickListener(view -> showTrackerOptions(tracker));

        llTrackerList.addView(viewTrackerItem);

    }

    public void showTrackerOptions(Tracker tracker) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout_tracker_options);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);
        }
        TextView tvTrackerOptionsTitle = dialog.findViewById(R.id.tvTrackerOptionTitle);
        tvTrackerOptionsTitle.setText(tracker.getPincode());

        LinearLayout llTrackerOptions = dialog.findViewById(R.id.llTrackerOptions);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Pause Option
        View pauseView = inflater.inflate(R.layout.dialog_list_item_tracker_option, llTrackerOptions, false);
        TextView tvPause = pauseView.findViewById(R.id.tvTrackerOption);
        tvPause.setText(R.string.pause);
        pauseView.setOnClickListener(view -> {
            trackerFacade.pause(tracker);
            dialog.dismiss();
            refreshTrackers();
        });
        llTrackerOptions.addView(pauseView);

        // Start Option
        View startView = inflater.inflate(R.layout.dialog_list_item_tracker_option, llTrackerOptions, false);
        TextView tvStart = startView.findViewById(R.id.tvTrackerOption);
        tvStart.setText(R.string.start);
        startView.setOnClickListener(view -> {
            trackerFacade.start(tracker);
            dialog.dismiss();
            refreshTrackers();
        });
        llTrackerOptions.addView(startView);

        // Show Details Option
        View showDetailsView = inflater.inflate(R.layout.dialog_list_item_tracker_option, llTrackerOptions, false);
        TextView tvShowDetails = showDetailsView.findViewById(R.id.tvTrackerOption);
        tvShowDetails.setText(R.string.show_details);
        showDetailsView.setOnClickListener(view -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(this, Uri.parse(COWIN_PORTAL));
            dialog.dismiss();
        });
        llTrackerOptions.addView(showDetailsView);

        dialog.show();
    }

    private void refreshTrackers() {

        LinearLayout llTrackerList = findViewById(R.id.llTrackerList);
        int childCount = llTrackerList.getChildCount();
        llTrackerList.removeViews(1, childCount - 1);

        // Show existing trackers
        List<Tracker> trackers = trackerFacade.getAllTrackers();
        trackers.forEach(this::addTracker);
    }

    public void check(View view){
        new AlarmReceiver().onReceive(this, new Intent());
    }
}
