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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSignInActivity extends AppCompatActivity {

    private EditText adminEmail,adminPassword;
    private Button adminButton;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);

        adminButton = findViewById(R.id.login_button_admin);
        adminEmail = findViewById(R.id.login_email_admin);
        adminPassword = findViewById(R.id.login_password_admin);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToAdminActivity();
            }
        });

    }

    private void SendUserToAdminActivity() {
        String email = adminEmail.getText().toString();
        String password = adminPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else if(!email.equals("admin@gmail.com")){
            Toast.makeText(this, "Please Enter valid email address", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                                finish();
                            }else{
                                Log.d("", "onComplete: ");
                                Toast.makeText(AdminSignInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
}
