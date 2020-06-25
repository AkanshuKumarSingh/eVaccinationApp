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

public class AdminCurrentApplicationsActivity extends AppCompatActivity {

    private Toolbar adminAppToolbar;
    private RecyclerView AdminAppRecyclerList;
    private DatabaseReference UsersRef,ChildRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_current_applications);
        adminAppToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.admin_app_main_page_toolbar);
        setSupportActionBar(adminAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Children List");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        AdminAppRecyclerList = findViewById(R.id.adminAppRecyclerView);
        AdminAppRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");

    }

    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Child> options =
                new FirebaseRecyclerOptions.Builder<Child>()
                .setQuery(ChildRef,Child.class)
                .build();

        FirebaseRecyclerAdapter<Child,SeeChildrenViewHolder> adapter
                = new FirebaseRecyclerAdapter<Child, SeeChildrenViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SeeChildrenViewHolder holder, int position, @NonNull Child model) {
                holder.childN.setText(model.getName());
                holder.childA.setText(model.getAge());
                holder.childG.setText(model.getGender());
                holder.childB.setText(model.getBloodGrp());
                holder.updateBtn.setText("Fix Appointment");
                final String userIds = getRef(position).getKey();

                ChildRef.child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        Log.d("kkk", "onDataChange: ");
                        if(dataSnapshot.exists()){

                            holder.updateBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(new Intent(getApplicationContext(),CreateAppointmentActivity.class));
                                    intent.putExtra("Id",userIds);
                                    intent.putExtra("name",dataSnapshot.child("name").getValue().toString());
                                    intent.putExtra("age",dataSnapshot.child("age").getValue().toString());
                                    intent.putExtra("gender",dataSnapshot.child("gender").getValue().toString());
                                    intent.putExtra("bloodGrp",dataSnapshot.child("bloodGrp").getValue().toString());
                                    intent.putExtra("date",dataSnapshot.child("date").getValue().toString());
                                    intent.putExtra("height",dataSnapshot.child("height").getValue().toString());
                                    intent.putExtra("weight",dataSnapshot.child("weight").getValue().toString());
                                    intent.putExtra("hospital",dataSnapshot.child("hospital").getValue().toString());
                                    intent.putExtra("parentId",dataSnapshot.child("parentId").getValue().toString());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public SeeChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_display_layout, viewGroup, false);
                SeeChildrenViewHolder viewHolder = new SeeChildrenViewHolder(view);
                return viewHolder;
            }
        };

        AdminAppRecyclerList.setAdapter(adapter);
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
