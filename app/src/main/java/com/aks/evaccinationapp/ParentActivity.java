package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ParentActivity extends AppCompatActivity {
    Button logoutBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        mAuth = FirebaseAuth.getInstance();

        Log.d("THISIS", "onCreate: " + mAuth.getCurrentUser().getUid());
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                new Intent(getApplicationContext(),SignInPage.class);
                finish();
            }
        });
    }
}
