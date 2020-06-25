package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import androidx.appcompat.widget.Toolbar;


public class VaccineReminderActivity extends AppCompatActivity {
    private Toolbar vaccineToolbar;
    private RecyclerView vaccineRecyclerView;
    private DatabaseReference UsersRef,ConfirmedRef,VaccinedRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_reminder);
        vaccineToolbar = findViewById(R.id.vaccine_app_main_page_toolbar);
        setSupportActionBar(vaccineToolbar);
        getSupportActionBar().setTitle("Vaccine Dates");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vaccineRecyclerView = findViewById(R.id.vaccineAppRecyclerView);
        vaccineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Assign");
        ConfirmedRef = FirebaseDatabase.getInstance().getReference().child("Confirmed");
        VaccinedRef = FirebaseDatabase.getInstance().getReference().child("Vaccined");
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Child> options =
                new FirebaseRecyclerOptions.Builder<Child>()
                        .setQuery(UsersRef, Child.class)
                        .build();

        FirebaseRecyclerAdapter<Child, SeeChildrenViewHolder1> adapter =
                new FirebaseRecyclerAdapter<Child, SeeChildrenViewHolder1>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SeeChildrenViewHolder1 holder, int position, @NonNull Child model)
                    {
                        final String userIds = getRef(position).getKey();
                        FirebaseDatabase.getInstance().getReference().child("Confirmed").addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(userIds)){
                                            ConfirmedRef.child(userIds).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        holder.childNameV.setText("Name : " + dataSnapshot.child("name").getValue().toString());
                                                        holder.childHospitalV.setText("Hospital : " +dataSnapshot.child("hospital").getValue().toString()
                                                        + "\n\nAppointment : " + dataSnapshot.child("appointment").getValue().toString()
                                                        );
                                                        holder.doneBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                doAllStuff(dataSnapshot.child("name").getValue().toString() + currentUserId);
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }
                        );

                    }

                    @NonNull
                    @Override
                    public SeeChildrenViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vaccinedlayout, viewGroup, false);
                        SeeChildrenViewHolder1 viewHolder = new SeeChildrenViewHolder1(view);
                        return viewHolder;
                    }
                };
        vaccineRecyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void doAllStuff(String childId) {
        UsersRef.child(childId).removeValue();
        VaccinedRef.child(childId).setValue("");
        moveGameRoom(ConfirmedRef.child(childId),VaccinedRef.child(childId));
        ConfirmedRef.child(childId).removeValue();
    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static class SeeChildrenViewHolder1 extends RecyclerView.ViewHolder {

        TextView childNameV,childHospitalV;
        Button doneBtn;

        public SeeChildrenViewHolder1(@NonNull View itemView) {
            super(itemView);

            childNameV = itemView.findViewById(R.id.nameTextViewV);
            childHospitalV = itemView.findViewById(R.id.hospitalTextViewV);
            doneBtn = itemView.findViewById(R.id.doneBtn);
        }

    }

}
