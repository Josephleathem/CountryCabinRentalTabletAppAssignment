package com.example.countrycabinrentaltabletappassignment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int STAY_NIGHTS = 3;
    private static final double CABIN_ONE_NIGHTLY_RATE = 225.00;
    private static final double CABIN_TWO_NIGHTLY_RATE = 180.00;

    private static final String STATE_CABIN_NAME = "state_cabin_name";
    private static final String STATE_NIGHTLY_RATE = "state_nightly_rate";
    private static final String STATE_CHECK_IN_MILLIS = "state_check_in_millis";
    private static final String STATE_CHECK_OUT_MILLIS = "state_check_out_millis";
    private static final String STATE_TOTAL_COST = "state_total_cost";

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private BookingData currentBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup cabinRadioGroup = findViewById(R.id.cabinRadioGroup);
        Button selectDateButton = findViewById(R.id.selectDateButton);
        ImageView cabinImage = findViewById(R.id.cabinImage);
        TextView cabinDescription = findViewById(R.id.cabinDescription);
        TextView cabinValue = findViewById(R.id.cabinValue);
        TextView datesValue = findViewById(R.id.datesValue);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CABIN_NAME)) {
            currentBooking = new BookingData(
                savedInstanceState.getString(STATE_CABIN_NAME, ""),
                savedInstanceState.getDouble(STATE_NIGHTLY_RATE, 0.0),
                savedInstanceState.getLong(STATE_CHECK_IN_MILLIS, 0L),
                savedInstanceState.getLong(STATE_CHECK_OUT_MILLIS, 0L),
                savedInstanceState.getDouble(STATE_TOTAL_COST, 0.0)
            );
            cabinValue.setText(currentBooking.cabinName);
            datesValue.setText(formatDateRange(currentBooking.checkInMillis, currentBooking.checkOutMillis));
        }

        cabinRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCabinOne) {
                cabinImage.setImageResource(R.drawable.mountain_view_cabin);
                cabinImage.setContentDescription(getString(R.string.cabin_one_name));
                cabinDescription.setText(R.string.cabin_one_description);
            } else if (checkedId == R.id.radioCabinTwo) {
                cabinImage.setImageResource(R.drawable.cabin_in_the_woods);
                cabinImage.setContentDescription(getString(R.string.cabin_two_name));
                cabinDescription.setText(R.string.cabin_two_description);
            }
        });

        cabinRadioGroup.check(R.id.radioCabinOne);

        selectDateButton.setOnClickListener(v -> {
            int checkedId = cabinRadioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Please select a cabin first.", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar today = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        int selectedCabinId = cabinRadioGroup.getCheckedRadioButtonId();
                        RadioButton selectedCabinButton = findViewById(selectedCabinId);
                        String selectedCabinName = selectedCabinButton.getText().toString();
                        double nightlyRate = getNightlyRate(selectedCabinId);

                        Calendar firstNight = Calendar.getInstance();
                        firstNight.set(Calendar.YEAR, year);
                        firstNight.set(Calendar.MONTH, month);
                        firstNight.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        firstNight.set(Calendar.HOUR_OF_DAY, 0);
                        firstNight.set(Calendar.MINUTE, 0);
                        firstNight.set(Calendar.SECOND, 0);
                        firstNight.set(Calendar.MILLISECOND, 0);

                        Calendar endDate = (Calendar) firstNight.clone();
            endDate.add(Calendar.DAY_OF_MONTH, STAY_NIGHTS);

            long checkInMillis = firstNight.getTimeInMillis();
            long checkOutMillis = endDate.getTimeInMillis();
            double totalCost = nightlyRate * STAY_NIGHTS;

            currentBooking = new BookingData(
                selectedCabinName,
                nightlyRate,
                checkInMillis,
                checkOutMillis,
                totalCost
            );

            cabinValue.setText(currentBooking.cabinName);
            datesValue.setText(formatDateRange(currentBooking.checkInMillis, currentBooking.checkOutMillis));
                    },
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentBooking == null) {
            return;
        }

        outState.putString(STATE_CABIN_NAME, currentBooking.cabinName);
        outState.putDouble(STATE_NIGHTLY_RATE, currentBooking.nightlyRate);
        outState.putLong(STATE_CHECK_IN_MILLIS, currentBooking.checkInMillis);
        outState.putLong(STATE_CHECK_OUT_MILLIS, currentBooking.checkOutMillis);
        outState.putDouble(STATE_TOTAL_COST, currentBooking.totalCost);
    }

    private String formatDateRange(long checkInMillis, long checkOutMillis) {
        return formatDate(checkInMillis) + " - " + formatDate(checkOutMillis);
    }

    private String formatDate(long dateMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillis);
        return dateFormatter.format(calendar.getTime());
    }

    private double getNightlyRate(int cabinId) {
        if (cabinId == R.id.radioCabinTwo) {
            return CABIN_TWO_NIGHTLY_RATE;
        }
        return CABIN_ONE_NIGHTLY_RATE;
    }

    private static class BookingData {
        private final String cabinName;
        private final double nightlyRate;
        private final long checkInMillis;
        private final long checkOutMillis;
        private final double totalCost;

        private BookingData(String cabinName, double nightlyRate, long checkInMillis, long checkOutMillis, double totalCost) {
            this.cabinName = cabinName;
            this.nightlyRate = nightlyRate;
            this.checkInMillis = checkInMillis;
            this.checkOutMillis = checkOutMillis;
            this.totalCost = totalCost;
        }
    }
}