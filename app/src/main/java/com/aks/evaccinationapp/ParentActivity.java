package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference RootRef;
    private CardView addBabyDetails,viewBabyDetails,myVaccineReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mToolbar = findViewById(R.id.main_page_toolbar);
        RootRef = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("eVaccination");

        InitializaAll();

        if(mFirebaseUser != null)
        Log.d("THISIS", "onCreate: " + mAuth.getCurrentUser().getUid());



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                new Intent(getApplicationContext(),SignInPage.class);
                finish();
            }
        });

        addBabyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBaby();
            }
        });

        viewBabyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewBaby();
            }
        });

        myVaccineReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeeVaccineDate();
            }
        });

    }

    private void SeeVaccineDate() {

    }

    private void ViewBaby() {

    }

    private void AddBaby() {
        startActivity(new Intent(getApplicationContext(),AddChildDetailsActivity.class));
    }

    private void InitializaAll() {
        logoutBtn = findViewById(R.id.logoutBtn);
        addBabyDetails = findViewById(R.id.AddChildDetails);
        viewBabyDetails = findViewById(R.id.ViewChildDetails);
        myVaccineReminder = findViewById(R.id.MyVaccineReminder);
    }


}
