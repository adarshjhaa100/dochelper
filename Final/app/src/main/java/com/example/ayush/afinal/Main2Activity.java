package com.example.ayush.afinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Java program to convert  Millisecond to Date in Java. Java API provides utility
 * method to get millisecond from Date and convert Millisecond to Date in Java.
 * @author http://javarevisited.blogspot.com
 */

public class Main2Activity extends AppCompatActivity {
CalendarView cv;
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cv = findViewById(R.id.calender);
        tv = findViewById(R.id.fb_slot);
        cv.setDate(System.currentTimeMillis());
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                long date = cv.getDate();
                Random rand = new Random();
                int randnum = rand.nextInt(3)+1;
                tv.setText("SLOT NO: " + Integer.toString(1));
                startActivity(new Intent(Main2Activity.this,PatientStart.class));
            }
        });

    }
}
