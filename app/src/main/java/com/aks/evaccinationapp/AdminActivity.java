package com.aks.evaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private CardView adminGetDetail,adminGetAssigned, totalVaccinedReport;
    private Button adminLogoutBtn;
    private FirebaseAuth mAuth;
    private Toolbar AdminToolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AdminToolbar1 = findViewById(R.id.admin_main_page_toolbar1);
        setSupportActionBar(AdminToolbar1);
        getSupportActionBar().setTitle("Admin");


        adminGetDetail = findViewById(R.id.getAllBabyDetails);
        adminGetAssigned = findViewById(R.id.showAssignedReport);
        totalVaccinedReport =findViewById(R.id.totalVaccinedReport);
        adminLogoutBtn = findViewById(R.id.adminlogoutBtn);
        mAuth = FirebaseAuth.getInstance();

        adminGetDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminCurrentApplicationsActivity.class));
            }
        });

        adminGetAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AssignedReportActivity.class));
            }
        });

        totalVaccinedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),totalReportActivity.class));
            }
        });

        adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),SignInPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
