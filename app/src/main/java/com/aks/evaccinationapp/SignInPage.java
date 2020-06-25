package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private CardView adminCardView, parentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        mAuth =FirebaseAuth.getInstance();
        adminCardView = findViewById(R.id.adminSignIn);
        parentCardView = findViewById(R.id.parentSignIn);

        adminCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToAdminSignIn();
            }
        });
        parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });
    }

    private void sendUserToAdminSignIn() {
        startActivity(new Intent(getApplicationContext(),AdminSignInActivity.class));
    }

    private void SendUserToLoginActivity() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currenUser = mAuth.getCurrentUser();
        if(currenUser == null){
            //Donothing
        }else{
            if(currenUser.getUid().equals("Bo1rR7v0dBMQHqG6JA42vZkunJO2")){
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                finish();
            }else
            SendUserToParentActivity();
        }
    }

    private void SendUserToParentActivity() {
        startActivity(new Intent(getApplicationContext(),ParentActivity.class));
        finish();
    }
}

