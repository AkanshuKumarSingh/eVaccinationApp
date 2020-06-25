package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.HashMap;

public class AddChildDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialSpinner spinner,spinnerBlood,HospitalSpinner;
    private Button selectDateButton,saveButton;
    private EditText childName,childAge,childHeight,childWeight;
    private TextView Hospital;
    private DatabaseReference ChildRef,UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String name,age,height,weight,hospital,bloodGrp,gender,date;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_details);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Fill Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        InitializaAll();

        spinner.setItems("Male", "Female");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                gender = item.toString();
            }
        });

        spinnerBlood.setItems("A+","A-","B+","B-","O+","O-","AB+","AB-");
        spinnerBlood.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                bloodGrp = item.toString();
            }
        });

        HospitalSpinner.setItems(
                "Aakash Healthcare Super Speciality Hosptial, Delhi",
                "Batra Hospital & Medical Research Centre, Delhi",
                "Bhagwati Hospital, Delhi",
                "Bhatia Global Hospital & Endosurgery Institute, Delhi",
                "BLK Super Speciality Hospital, Delhi",
                "Jeewan Hospital & Nursing Home Pvt Ltd, Delhi",
                "Maharishi Ayurveda Hospital, Delhi"
        );
        HospitalSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                hospital = item.toString();
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        age = childAge.getText().toString();
        name = childName.getText().toString();
        height = childHeight.getText().toString();
        weight = childWeight.getText().toString();

        if(gender == null){
            gender = "Male";
        }
        if(bloodGrp == null){
            bloodGrp = "A+";
        }
        if(hospital == null){
            hospital = "Aakash Healthcare Super Speciality Hosptial, Delhi";
        }

        if(TextUtils.isEmpty(age)){
            Toast.makeText(this, "Please fill age", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please fill name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(height)){
            Toast.makeText(this, "Please fill height", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(weight)){
            Toast.makeText(this, "Please fill weight", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(hospital)){
            Toast.makeText(this, "Please fill hospital", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please fill gender", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(date)){
            Toast.makeText(this, "Please fill date", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bloodGrp)){
            Toast.makeText(this, "Please fill BloodGroup", Toast.LENGTH_SHORT).show();
        }else{
            String childKey = name + currentUserId;
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",name);
            map.put("age",age);
            map.put("gender",gender);
            map.put("date",date);
            map.put("bloodGrp",bloodGrp);
            map.put("height",height);
            map.put("weight",weight);
            map.put("hospital",hospital);
            map.put("parentId",currentUserId);
            ChildRef.child(childKey).setValue(map);
            UsersRef.child(currentUserId).child("notVaccined").child(childKey).child("Saved").setValue("");
            startActivity(new Intent(getApplicationContext(),ParentActivity.class));
            finish();
        }

    }

    private void InitializaAll(){
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinnerBlood = (MaterialSpinner) findViewById(R.id.spinnerBlood);
        HospitalSpinner = (MaterialSpinner) findViewById(R.id.HospitalSpinner);
        selectDateButton = findViewById(R.id.selectDateButton);
        saveButton = findViewById(R.id.saveButton);
        childAge = findViewById(R.id.childAge);
        childName = findViewById(R.id.childName);
        childHeight = findViewById(R.id.childHeight);
        childWeight = findViewById(R.id.childWeight);
        Hospital = findViewById(R.id.Hospital);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");

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
        selectDateButton.setText("Date: " + date);
    }
}
