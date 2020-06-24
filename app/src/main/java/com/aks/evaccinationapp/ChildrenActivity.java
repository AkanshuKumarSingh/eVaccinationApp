package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChildrenActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView ChildRecyclerList;
    private DatabaseReference UsersRef,ChildRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.children_main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Children List");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        ChildRecyclerList = findViewById(R.id.childRecyclerView);
        ChildRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("1111", "onStart: ");

        FirebaseRecyclerOptions<Child> options =
                new FirebaseRecyclerOptions.Builder<Child>()
                        .setQuery(UsersRef, Child.class)
                        .build();

        Log.d("111111", "onStart: ");

        FirebaseRecyclerAdapter<Child, SeeChildrenViewHolder> adapter =
            new FirebaseRecyclerAdapter<Child, SeeChildrenViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final SeeChildrenViewHolder holder, int position, @NonNull Child model) {
                    Log.d("111111", "onBindViewHolder: ");
                    String userIds = getRef(position).getKey();
                    Log.d("111110", "onBindViewHolder: " + userIds);

                    ChildRef.child(userIds).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                holder.childN.setText("Name : " + dataSnapshot.child("name").getValue().toString());
                                holder.childA.setText("Age : " +dataSnapshot.child("age").getValue().toString());
                                holder.childG.setText("Gender : " + dataSnapshot.child("gender").getValue().toString());
                                holder.childB.setText("BloogGrp : " + dataSnapshot.child("bloodGrp").getValue().toString());
                                holder.updateBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(new Intent(getApplicationContext(),UpdateChildActivity.class));
                                        intent.putExtra("name",dataSnapshot.child("name").getValue().toString());
                                        intent.putExtra("age",dataSnapshot.child("age").getValue().toString());
                                        intent.putExtra("gender",dataSnapshot.child("gender").getValue().toString());
                                        intent.putExtra("bloodGrp",dataSnapshot.child("bloodGrp").getValue().toString());
                                        intent.putExtra("date",dataSnapshot.child("date").getValue().toString());
                                        intent.putExtra("height",dataSnapshot.child("height").getValue().toString());
                                        intent.putExtra("weight",dataSnapshot.child("weight").getValue().toString());
                                        intent.putExtra("hospital",dataSnapshot.child("hospital").getValue().toString());
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
        ChildRecyclerList.setAdapter(adapter);
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
