package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.HashMap;

public class UpdateChildActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private MaterialSpinner spinnerUpdate,spinnerBloodUpdate,hospitalSpinnerChild;
    private Button selectDateButtonUpdate,saveButtonUpdate;
    private EditText childNameUpdate,childAgeUpdate,childHeightUpdate,childWeightUpdate;
    private DatabaseReference ChildRef,UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String name,age,height,weight,hospital,bloodGrp,gender,date;
    private Toolbar mToolbarUpdate;
    private TextView genderO,bloodO,HospitalO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child);

        mToolbarUpdate = findViewById(R.id.main_page_toolbar_update);
        setSupportActionBar(mToolbarUpdate);
        getSupportActionBar().setTitle("Update Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        InitializeAll();

        spinnerUpdate.setItems("Male", "Female");
        spinnerUpdate.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                gender = item.toString();
            }
        });

        spinnerBloodUpdate.setItems("A+","A-","B+","B-","O+","O-","AB+","AB-");
        spinnerBloodUpdate.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                bloodGrp = item.toString();
            }
        });

        hospitalSpinnerChild.setItems(
                "Aakash Healthcare Super Speciality Hosptial, Delhi",
                "Batra Hospital & Medical Research Centre, Delhi",
                "Bhagwati Hospital, Delhi",
                "Bhatia Global Hospital & Endosurgery Institute, Delhi",
                "BLK Super Speciality Hospital, Delhi",
                "Jeewan Hospital & Nursing Home Pvt Ltd, Delhi",
                "Maharishi Ayurveda Hospital, Delhi"
                );
        hospitalSpinnerChild.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                hospital = item.toString();
            }
        });

        selectDateButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        saveButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    private void InitializeAll() {

        name = getIntent().getExtras().get("name").toString();
        age = getIntent().getExtras().get("age").toString();
        height = getIntent().getExtras().get("height").toString();
        weight = getIntent().getExtras().get("weight").toString();
        hospital = getIntent().getExtras().get("hospital").toString();
        bloodGrp = getIntent().getExtras().get("bloodGrp").toString();
        gender = getIntent().getExtras().get("gender").toString();
        date = getIntent().getExtras().get("date").toString();

        spinnerUpdate = (MaterialSpinner) findViewById(R.id.spinnerUpdate);
        spinnerBloodUpdate = (MaterialSpinner) findViewById(R.id.spinnerBloodUpdate);
        selectDateButtonUpdate = findViewById(R.id.selectDateButtonUpdate);
        saveButtonUpdate = findViewById(R.id.saveButtonUpdate);
        childAgeUpdate = findViewById(R.id.childAgeUpdate);
        childNameUpdate = findViewById(R.id.childNameUpdate);
        childHeightUpdate = findViewById(R.id.childHeightUpdate);
        childWeightUpdate = findViewById(R.id.childWeightUpdate);
        hospitalSpinnerChild = findViewById(R.id.HospitalSpinnerUpdate);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        bloodO = findViewById(R.id.bloodO);
        genderO = findViewById(R.id.genderO);
        HospitalO = findViewById(R.id.HospitalO);

        spinnerUpdate.setText(gender);
        spinnerBloodUpdate.setText(bloodGrp);
        selectDateButtonUpdate.setText(date);
        saveButtonUpdate.setText("Update");
        childWeightUpdate.setText(weight);
        childAgeUpdate.setText(age);
        childNameUpdate.setText(name);
        childHeightUpdate.setText(height);
        HospitalO.setText(hospital);
        bloodO.setText("bloodGrp : " + bloodGrp);
        genderO.setText("Gender : " + gender);

        bloodO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerBloodUpdate.setVisibility(View.VISIBLE);
                bloodO.setText("bloodGrp : ");
            }
        });

        genderO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerUpdate.setVisibility(View.VISIBLE);
                genderO.setText("gender : ");
            }
        });

        HospitalO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospitalSpinnerChild.setVisibility(View.VISIBLE);
                HospitalO.setText("Hospital : ");
            }
        });

    }

    private void saveData() {

        age = childAgeUpdate.getText().toString();
        name = childNameUpdate.getText().toString();
        height = childHeightUpdate.getText().toString();
        weight = childWeightUpdate.getText().toString();

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
//          UsersRef.child(currentUserId).child(childKey).child("Saved").setValue("");

            startActivity(new Intent(getApplicationContext(),ParentActivity.class));
            finish();
        }

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
        selectDateButtonUpdate.setText("Date: " + date);
    }

}
