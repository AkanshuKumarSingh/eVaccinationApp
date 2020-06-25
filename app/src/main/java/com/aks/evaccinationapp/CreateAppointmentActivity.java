package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CreateAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView caChildName,caChildAge,caChildGender,caChildBloodGrp,cachildHeight,caChildWeight,caChildHospital;
    private Button caButton,caSaveBtn;
    private String date, time;
    private int mHour,mMinute;
    private DatabaseReference ConfirmRef,ChildRef;
    private Toolbar caToolbar;
    private String id = "";
    private String parentId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        caToolbar = findViewById(R.id.create_appointment_page_toolbar);
        setSupportActionBar(caToolbar);
        getSupportActionBar().setTitle("Fix Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        InitializeAll();

        caButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date == null)
                showDatePickerDialog();
                if(date != null){
                    showTimePickerDialog();
                }

            }
        });

        caSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildRef.child("appointment").setValue(date + " " + time);
                moveGameRoom(ChildRef,ConfirmRef.child(id));
                DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Children");
                del.child(id).removeValue();
                final DatabaseReference parentDel = FirebaseDatabase.getInstance().getReference().child("Users").child(parentId).child("notVaccined");
                DatabaseReference assignRef = FirebaseDatabase.getInstance().getReference().child("Users").child(parentId).child("Assign");
                assignRef.child(id).setValue("");
                moveGameRoom(parentDel.child(id),assignRef.child(id));

                Thread thread = new Thread() {
                    public void run() {
                        Looper.prepare();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                parentDel.child(id).removeValue();
                                startActivity(new Intent(getApplicationContext(),AdminCurrentApplicationsActivity.class));
                                finish();
                            }
                        }, 1000);

                        Looper.loop();
                    }
                };
                thread.start();

            }
        });

    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showTimePickerDialog(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if(hourOfDay > 12){
                            time = hourOfDay + ":" + minute + " PM";
                        }else{
                            time = hourOfDay + ":" + minute + " AM";
                        }
                        caButton.setText(date + " " + time);
                    }
                },mHour,mMinute,false);

        timePickerDialog.show();
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date = i + "/" + i1 + "/" + i2;
        caButton.setText("Select Time");
    }



    private void InitializeAll(){
        caChildName = findViewById(R.id.create_appointment_child_name);
        caChildAge = findViewById(R.id.create_appointment_child_age);
        caChildGender = findViewById(R.id.create_appointment_child_gender);
        caChildBloodGrp = findViewById(R.id.create_appointment_bloodGrp);
        cachildHeight = findViewById(R.id.create_appointment_assign_height);
        caChildWeight = findViewById(R.id.create_appointment_assign_weight);
        caChildHospital = findViewById(R.id.create_appointment_assign_hospital);
        caButton = findViewById(R.id.create_appointment_assign_date);
        caSaveBtn = findViewById(R.id.create_appointment_assign_saveBtn);
        try {
            parentId = getIntent().getExtras().get("parentId").toString();
            id = getIntent().getExtras().get("Id").toString();
            caChildName.setText(getIntent().getExtras().get("name").toString());
            caChildAge.setText(getIntent().getExtras().get("age").toString());
            caChildGender.setText(getIntent().getExtras().get("gender").toString());
            caChildBloodGrp.setText(getIntent().getExtras().get("bloodGrp").toString());
            cachildHeight.setText(getIntent().getExtras().get("height").toString());
            caChildWeight.setText(getIntent().getExtras().get("weight").toString());
            caChildHospital.setText(getIntent().getExtras().get("hospital").toString());

            Log.d("kkk", "InitializeAll: " + getIntent().getExtras().get("hospital").toString());

        }catch (Exception e){
            Log.d("kkk", "InitializeAll: " + "exception " + e.getMessage());
        }
        if(id.equals(""))
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children").child(id);
        else
           ChildRef =  FirebaseDatabase.getInstance().getReference().child("Children").child(id);
        ConfirmRef = FirebaseDatabase.getInstance().getReference().child("Confirmed");


    }

}
