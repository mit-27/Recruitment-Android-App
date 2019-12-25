package com.example.recruitmentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.ValueEventListener;

public class Candidate_view extends AppCompatActivity {
    RecyclerView menuRecyclerView;
    Cadapter cadapter;
    ArrayList<Candidate> candidates = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_view);




        menuRecyclerView = findViewById(R.id.candidate_itemview);
        LinearLayoutManager manager = new LinearLayoutManager(Candidate_view.this);
        menuRecyclerView.setLayoutManager(manager);
        // menuRecyclerView.setAdapter(menuAdapter);

        registerForContextMenu(menuRecyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("Candidates_details");

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),add_candidate.class));
                finish();
            }
        });
    }

    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                candidates.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Candidate item = ds.getValue(Candidate.class);
                    candidates.add(item);
                }


                cadapter = new Cadapter(candidates,Candidate_view.this);
                menuRecyclerView.setAdapter(cadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean onContextItemSelected(MenuItem item)
    {

        switch(item.getItemId()){
            case 101:
                cadapter.updateItem(item.getGroupId());
                return true;
            case 102:
                cadapter.removeItem(item.getGroupId());
                return true;
        }
        return false;
    }

}
