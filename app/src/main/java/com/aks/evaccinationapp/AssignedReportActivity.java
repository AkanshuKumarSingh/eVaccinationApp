package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssignedReportActivity extends AppCompatActivity {
    private RecyclerView adminRecyclerView;
    private DatabaseReference UsersRef,ConfirmedRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_report);

        Toolbar admin_assigned_main_page_toolbar = findViewById(R.id.admin_assigned_main_page_toolbar);
        setSupportActionBar(admin_assigned_main_page_toolbar);
        getSupportActionBar().setTitle("Confirmed babies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adminRecyclerView = findViewById(R.id.adminRecyclerView);
        adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        ConfirmedRef = FirebaseDatabase.getInstance().getReference().child("Confirmed");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Child> options =
                new FirebaseRecyclerOptions.Builder<Child>()
                        .setQuery(ConfirmedRef, Child.class)
                        .build();


        FirebaseRecyclerAdapter<Child, ChildrenActivity.SeeChildrenViewHolder> adapter =
                new FirebaseRecyclerAdapter<Child, ChildrenActivity.SeeChildrenViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChildrenActivity.SeeChildrenViewHolder holder, int position, @NonNull Child model) {
                        Log.d("111111", "onBindViewHolder: ");
                        holder.childN.setText("Name : " + model.getName());
                        holder.childA.setText("Age : " + model.getAge());
                        holder.childG.setText("Gender : " + model.getGender());
                        holder.childB.setText("BloodGrp : " + model.getBloodGrp());
                        holder.updateBtn.setVisibility(View.GONE);
                    }

                    @NonNull
                    @Override
                    public ChildrenActivity.SeeChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_display_layout, viewGroup, false);
                        ChildrenActivity.SeeChildrenViewHolder viewHolder = new ChildrenActivity.SeeChildrenViewHolder(view);
                        return viewHolder;
                    }
                };
        adminRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class SeeChildrenViewHolder extends RecyclerView.ViewHolder {

        TextView childN, childA,childG,childB;
        Button updateBtn;

        public SeeChildrenViewHolder(@NonNull View itemView) {
            super(itemView);

            childN = itemView.findViewById(R.id.nameTextView);
            childA = itemView.findViewById(R.id.ageTextView);
            childG = itemView.findViewById(R.id.genderTextView);
            childB = itemView.findViewById(R.id.bloodTextView);
            updateBtn = itemView.findViewById(R.id.updateBtn);
        }

    }


}
