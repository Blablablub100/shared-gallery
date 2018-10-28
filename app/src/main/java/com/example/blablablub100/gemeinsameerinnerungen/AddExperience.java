package com.example.blablablub100.gemeinsameerinnerungen;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.ExperienceReader;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExperience extends AppCompatActivity {

    Calendar startDateCalendar = Calendar.getInstance();
    Calendar endDateCalendar = Calendar.getInstance();
    TextView chooseStartView = null;
    TextView chooseEndView = null;
    EditText name = null;
    EditText description = null;

    // OnDateListener zum Setzen des Startdatums
    DatePickerDialog.OnDateSetListener startDateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, monthOfYear);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(chooseStartView, startDateCalendar);
        }
    };

    // Öffnet den Datepicker und ruft den OnDateListener auf für Startdatum
    public void chooseStart() {
        new DatePickerDialog(AddExperience.this, startDateSet, startDateCalendar
                .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    // OnDateListener zum Setzen des Enddatums
    DatePickerDialog.OnDateSetListener endDateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(chooseEndView, endDateCalendar);
        }
    };

    public void chooseEnd() {
        new DatePickerDialog(AddExperience.this, endDateSet, endDateCalendar
                .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Setzt TextView auf Datum
    private void updateLabel(TextView tv, Calendar cal) {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        tv.setText(sdf.format(cal.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        name = findViewById(R.id.editText_exp_name);
        description = findViewById(R.id.editText_exp_description);
        chooseStartView = (TextView) findViewById(R.id.textView_exp_choose_startdate);
        chooseStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStart();
            }
        });
        chooseEndView = (TextView) findViewById(R.id.textView_exp_choose_enddate);
        chooseEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseEnd();
            }
        });
        Button saveButton = findViewById(R.id.button_exp_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Experience(startDateCalendar.getTime()
                        , endDateCalendar.getTime()
                        , name.getText().toString()
                        , description.getText().toString()).write();
                Intent launchMain = new Intent(getBaseContext(), MainActivity.class);
                startActivity(launchMain);
            }
        });
    }





}
