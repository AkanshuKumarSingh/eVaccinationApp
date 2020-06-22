package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class EmailActivity extends AppCompatActivity {
    private EditText emailText,passwordText;
    private Button loginBtn;
    private TextView needNewAccount;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        InitializeAll();

        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

    }

    private void RegisterUser() {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }

    private void InitializeAll() {
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_button);
        needNewAccount = findViewById(R.id.need_new_account_link);
    }
}
