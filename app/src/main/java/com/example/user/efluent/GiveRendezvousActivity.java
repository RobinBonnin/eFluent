package com.example.user.efluent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GiveRendezvousActivity extends AppCompatActivity implements
        View.OnClickListener {


    int year, monthOfYear, dayOfMonth;
    int hour, minute;
    public static LoginManager login;
    public ArrayList<Patient> patient_list;
    public static Patient patient;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private Integer mYear, mMonth, mDay, mHour, mMinute;

    private Spinner mySpinner;
    private SpinAdapter spinAdapter;

    public Patient choosenPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rendezvous);

        final GiveRendezvousActivity self = this;

        btnDatePicker = (Button)findViewById(R.id.buttonDatePicker);
        btnTimePicker = (Button)findViewById(R.id.buttonTimePicker);

        txtDate=(EditText)findViewById(R.id.DateRDV);
        txtTime=(EditText)findViewById(R.id.TimeRDV);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        login.patientList2(self);

        final Button buttonFix = (Button) findViewById(R.id.buttonFixRDV);
        buttonFix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> ProActivity tab");
                //datechoisie.set(mYear + 1900, mMonth, mDay, mHour, mMinute);
                //Object Date is deprecated, better use Calendar!


                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth, hour, minute, 0);
       /*         Log.i("test", mYear.toString());
                Log.i("test", mMonth.toString());
                Log.i("test", mDay.toString());
                Log.i("test", mHour.toString());
                Log.i("test", mMinute.toString());*/

                // The following doesn't work, I have no idea why! FIXME
                Date date = calendar.getTime();
                //Log.i("test", date.toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String formatedDate = df.format(date);
                Log.i("test", formatedDate );

                // String formatedDate = "hola";
                // Log.i("test", formatedDate);

                //login.createRDV(self, choosenPatient, "formatedDate");
                login.createRDV(self, spinAdapter.getItem(mySpinner.getSelectedItemPosition()), formatedDate);

                ProActivity.login = login;
                Intent intent = new Intent(v.getContext(), ProActivity.class);
                startActivity(intent);


            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            setDate(year, monthOfYear, dayOfMonth);
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            setTime(hourOfDay, minute);
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        //datechoisie.set(mYear + 1900, mMonth, mDay, mHour, mMinute);
        //Object Date is deprecated, better use Calendar!
    }

    public void setPatients (final ArrayList<Patient> patient_list){
        this.patient_list = patient_list;

        System.out.println("INSIDE GIVE_RENDEZVOUS_ACTIVITY!!!");
        System.out.println(patient_list.size());

        // Initialize the spinAdapter sending the current context
        // Send the simple_spinner_item layout
        // And finally send the Patient array (Your data)

        spinAdapter = new SpinAdapter(GiveRendezvousActivity.this,
                R.layout.spinner_row,
                patient_list);
        mySpinner = (Spinner) findViewById(R.id.spinnerListPatient);
        mySpinner.setAdapter(spinAdapter); // Set the custom adapter to the spinner */

        // You can create an anonymous listener to handle the event when is selected an spinner item
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /** TODO Pass the selected Patient to the server here **/
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Patient patient = spinAdapter.getItem(position);
                choosenPatient = patient;
                Log.i("test", Integer.toString(mySpinner.getSelectedItemPosition() ));
                // Here you can do the action you want to...
                /*Toast.makeText(GiveRendezvousActivity.this, "Name: " + patient.first_name,
                        Toast.LENGTH_SHORT).show();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


    }


    public void giveSuccess() {

    }

    private void setDate(int year,
                   int monthOfYear, int dayOfMonth){
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    private void setTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }
}
