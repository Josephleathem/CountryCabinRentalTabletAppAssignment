package com.example.countrycabinrentaltabletappassignment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String EXTRA_CABIN_NAME = "extra_cabin_name";
    public static final String EXTRA_CABIN_DESCRIPTION = "extra_cabin_description";
    public static final String EXTRA_DATE_RANGE = "extra_date_range";
    public static final String EXTRA_TOTAL_COST = "extra_total_cost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.confirmationRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView cabinValue = findViewById(R.id.confirmationCabinValue);
        TextView datesValue = findViewById(R.id.confirmationDatesValue);
        TextView totalValue = findViewById(R.id.confirmationTotalValue);
        TextView bookingMessageValue = findViewById(R.id.confirmationBookingMessageValue);
        Button reserveButton = findViewById(R.id.reserveButton);

        String cabinName = getIntent().getStringExtra(EXTRA_CABIN_NAME);
        String cabinDescription = getIntent().getStringExtra(EXTRA_CABIN_DESCRIPTION);
        String dateRange = getIntent().getStringExtra(EXTRA_DATE_RANGE);
        String totalCost = getIntent().getStringExtra(EXTRA_TOTAL_COST);

        cabinValue.setText(cabinName == null ? getString(R.string.placeholder_value) : cabinName);
        datesValue.setText(dateRange == null ? getString(R.string.placeholder_value) : dateRange);
        totalValue.setText(totalCost == null ? getString(R.string.placeholder_value) : totalCost);

        String safeCabinName = (cabinName == null || cabinName.trim().isEmpty())
            ? getString(R.string.placeholder_value)
            : cabinName;
        String safeCabinDescription = (cabinDescription == null || cabinDescription.trim().isEmpty())
            ? getString(R.string.placeholder_value)
            : cabinDescription;

        bookingMessageValue.setText(getString(
            R.string.confirmation_booking_message_format,
            safeCabinName,
            safeCabinDescription
        ));

        reserveButton.setOnClickListener(v -> {
            Toast.makeText(this, R.string.booking_confirmed_message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}