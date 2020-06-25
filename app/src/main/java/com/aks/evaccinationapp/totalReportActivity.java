package com.aks.evaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class totalReportActivity extends AppCompatActivity {
    private Toolbar admin_total_app_main_page_toolbar;
    private RecyclerView adminTotalAppRecyclerView;
    private DatabaseReference VaccinedRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_report);

        admin_total_app_main_page_toolbar = findViewById(R.id.admin_total_app_main_page_toolbar);
        setSupportActionBar(admin_total_app_main_page_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Total Report");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        adminTotalAppRecyclerView = findViewById(R.id.adminTotalAppRecyclerView);
        adminTotalAppRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        VaccinedRef = FirebaseDatabase.getInstance().getReference().child("Vaccined");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Child> options =
                new FirebaseRecyclerOptions.Builder<Child>()
                        .setQuery(VaccinedRef,Child.class)
                        .build();

        FirebaseRecyclerAdapter<Child,SeeChildrenViewHolderT> adapter
                = new FirebaseRecyclerAdapter<Child, SeeChildrenViewHolderT>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SeeChildrenViewHolderT holder, int position, @NonNull Child model) {
                holder.childNameT.setText(model.getName());
                final String userIds = getRef(position).getKey();

                FirebaseDatabase.getInstance().getReference().child("Vaccined").child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             if(dataSnapshot.exists()){
                                 holder.childHospitalT.setText(dataSnapshot.child("hospital").getValue().toString());
                                 holder.doneBtnT.setText("Success");
                             }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public SeeChildrenViewHolderT onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.total_report_layout, viewGroup, false);
                SeeChildrenViewHolderT viewHolder = new SeeChildrenViewHolderT(view);
                return viewHolder;
            }
        };

        adminTotalAppRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class SeeChildrenViewHolderT extends RecyclerView.ViewHolder {

        TextView childNameT,childHospitalT;
        Button doneBtnT;

        public SeeChildrenViewHolderT(@NonNull View itemView) {
            super(itemView);

            childNameT = itemView.findViewById(R.id.nameTextViewT);
            childHospitalT = itemView.findViewById(R.id.hospitalTextViewT);
            doneBtnT = itemView.findViewById(R.id.doneBtnT);
        }

    }

}
