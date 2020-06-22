package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.spec.ECField;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_email,register_password;
    private TextView already_have_account_link;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        already_have_account_link = findViewById(R.id.already_have_account_link);
        register_button = findViewById(R.id.register_button);


    }
}
