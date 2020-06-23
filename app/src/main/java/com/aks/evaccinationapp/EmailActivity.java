package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(EmailActivity.this,"Please enter email...",Toast.LENGTH_SHORT)
                            .show();
                }else if(TextUtils.isEmpty(password)){

                    Toast.makeText(EmailActivity.this,"Please enter password...",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    loadingBar.setTitle("Signing You In");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        startActivity(new Intent(getApplicationContext(),ParentActivity.class));
                                        finish();
                                    }else{
                                        Log.d("DEBUG", "onComplete: " + task.getException());
                                        Toast.makeText(EmailActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

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
        loadingBar = new ProgressDialog(this);
    }
}
