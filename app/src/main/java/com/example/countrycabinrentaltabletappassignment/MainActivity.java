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

                        Calendar firstNight = Calendar.getInstance();
                        firstNight.set(Calendar.YEAR, year);
                        firstNight.set(Calendar.MONTH, month);
                        firstNight.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        firstNight.set(Calendar.HOUR_OF_DAY, 0);
                        firstNight.set(Calendar.MINUTE, 0);
                        firstNight.set(Calendar.SECOND, 0);
                        firstNight.set(Calendar.MILLISECOND, 0);

                        Calendar endDate = (Calendar) firstNight.clone();
                        endDate.add(Calendar.DAY_OF_MONTH, 3);

                        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                        String formattedRange = formatter.format(firstNight.getTime())
                                + " - "
                                + formatter.format(endDate.getTime());

                        cabinValue.setText(selectedCabinName);
                        datesValue.setText(formattedRange);
                    },
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });
    }
}