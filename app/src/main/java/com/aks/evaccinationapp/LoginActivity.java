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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "HI";
    private EditText InputPhoneNumber,InputVerificationCode;
    private Button sendVerificationCodeButton, VerifyButton;
    private ProgressDialog loadingBar;
    private TextView signInWithEmail;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputPhoneNumber = findViewById(R.id.parentContactNo);
        sendVerificationCodeButton = findViewById(R.id.send_ver_code_button);
        VerifyButton = findViewById(R.id.ver_code_button);
        loadingBar = new ProgressDialog(this);
        InputVerificationCode = findViewById(R.id.InputVerificationCode);
        signInWithEmail = findViewById(R.id.sign_in_by_email);
        mAuth = FirebaseAuth.getInstance();

        signInWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = "+91" + InputPhoneNumber.getText().toString();

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(LoginActivity.this, "Please enter your phone number first...", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("please wait, while we are authenticating your device...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LoginActivity.this,               // Activity (for callback binding)
                            callbacks);
                }
            }
        });

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificationCode  = InputVerificationCode.getText().toString();

                if(TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(LoginActivity.this, "Please write code first...", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setTitle("Verification Code");
                    loadingBar.setMessage("please wait, while we are verifying you code...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("THISIS", "onVerificationFailed: " + e.getMessage());

                sendVerificationCodeButton.setVisibility(View.VISIBLE);
                InputPhoneNumber.setVisibility(View.VISIBLE);

                VerifyButton.setVisibility(View.INVISIBLE);
                InputVerificationCode.setVisibility(View.INVISIBLE);

            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);

                loadingBar.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();

                sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if(credential != null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Congratulations, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                            sendUserToParentActivity();

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this, "Error : " + message , Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendUserToParentActivity() {
        new Intent(getApplicationContext(),LoginActivity.class);
        finish();
    }

}
