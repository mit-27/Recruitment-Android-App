package com.example.recruitmentapp;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Schedule extends AppCompatActivity {
    private static Button date, time;
    private static TextView set_date, set_time;
    TextInputEditText et_can_name;
    Button btn_update_schedule;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    int year,month,day,hour,minute;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        // Action Bar and its Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Schedule");
        Log.i("Year and month",year+" and "+month);
        // enable back button
      //  actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
        date = (Button) findViewById(R.id.selectdate);
        time = (Button) findViewById(R.id.selecttime);
        set_date = (TextView) findViewById(R.id.set_date);
        set_time = (TextView) findViewById(R.id.set_time);
        et_can_name = findViewById(R.id.schedule_can_name);
        Intent i = getIntent();
//        et_can_name.setText(i.getStringExtra("Can_name"));

        date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Date dialog
                showDialog(Date_id);
            }
        });
        time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(Time_id);
            }
        });

        // Can info update and fetch

        String cid = i.getStringExtra("Can_id");
        Log.i("Can id : ",cid);
        String cname = i.getStringExtra("Can_name");
        if(!TextUtils.isEmpty(cname))
        {
            et_can_name.setText(cname);
        }
        String intv_date = i.getStringExtra("Can_int_date");
        String intv_time = i.getStringExtra("Can_int_time");

        Log.i("Date : ",intv_date);
        Log.i(" Time : ",intv_time);



        if(!TextUtils.isEmpty(intv_date))
        {
            List<String> date = Arrays.asList(intv_date.split("/"));
            Log.i("Test : ",date.get(0));
            month = Integer.valueOf(date.get(0));
            day = Integer.valueOf(date.get(1));
            year = Integer.valueOf(date.get(2));
            String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            set_date.setText(date1);

        }



        if(!TextUtils.isEmpty(intv_time))
        {
            List<String> date = Arrays.asList(intv_time.split(":"));
            hour = Integer.valueOf(date.get(0));
            minute = Integer.valueOf(date.get(1));
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            set_time.setText(time1);

        }

        Log.i("Set time : ",set_time.getText().toString());
        Log.i(" Set date",set_date.getText().toString());



        btn_update_schedule = findViewById(R.id.btn_schedule_update);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("Candidates_details").child(cid);

        btn_update_schedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(set_date.getText().toString()) || TextUtils.isEmpty(set_time.getText().toString()))
                {
                    Toast.makeText(Schedule.this, "Select the Date and Time", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.i("testing",set_date.getText().toString());
                    databaseReference.child("intv_date").setValue(set_date.getText().toString());
                    databaseReference.child("intv_time").setValue(set_time.getText().toString());
                    databaseReference.child("interview_status").setValue(true);
                    Toast.makeText(Schedule.this, "Interview Schedule Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Candidate_view.class));
                    finish();
                }
            }
        });



    }

    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        if(day==0 && hour==0)
        {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }



        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                return new DatePickerDialog(Schedule.this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(Schedule.this, time_listener, hour,
                        minute, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            set_date.setText(date1);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            set_time.setText(time1);
        }
    };
}