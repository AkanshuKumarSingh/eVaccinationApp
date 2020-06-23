package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.spec.ECField;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_email,register_password;
    private TextView already_have_account_link;
    private Button register_button;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitializaFields();

        already_have_account_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EmailActivity.class));
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });

    }

    private void InitializaFields() {
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        already_have_account_link = findViewById(R.id.already_have_account_link);
        register_button = findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);
    }

    private void CreateNewAccount() {
        String email = register_email.getText().toString();
        String password = register_password.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email...",Toast.LENGTH_SHORT)
                    .show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password...",Toast.LENGTH_SHORT)
                    .show();
        }else{
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating the account...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String currentUserId = mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserId).setValue("");

                                SendUserToParentActivity();
                                Toast.makeText(RegisterActivity.this,"Account Created Successfully...",Toast.LENGTH_SHORT)
                                        .show();
                                loadingBar.dismiss();
                            }
                            else{
                                loadingBar.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }

    private void SendUserToParentActivity() {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();
    }


}
