package com.example.recruitmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;

public class profile_page extends AppCompatActivity {

    MaterialBetterSpinner genderSpinner, jobTitleSpinner ;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RadioButton rdm,rdf,rdo;
    Button btn_profile_update;
    TextInputEditText et_name,et_email,et_phone,et_jobtitle;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Action Bar and its Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Profile");
        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
        progressDialog = new ProgressDialog(this);



        // init database
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("User_Info");

        // init views
        et_jobtitle = findViewById(R.id.input_job_Title);
        et_name = findViewById(R.id.input_name);
        et_email = findViewById(R.id.input_email);
        et_phone = findViewById(R.id.input_phone);
        rdm = findViewById(R.id.input_male);
        rdf = findViewById(R.id.input_female);
        btn_profile_update = findViewById(R.id.btn_profile_update);

        btn_profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Updating Profile");
                progressDialog.show();
                String Email = et_email.getText().toString();
                String Phone = et_phone.getText().toString();
                String Name = et_name.getText().toString();
                String Jobtitle = et_jobtitle.getText().toString();
                String Gender;
                if(rdm.isChecked())
                {
                    Gender = rdm.getText().toString();
                }
                else
                {
                    Gender = rdf.getText().toString();
                }



                HashMap<String,Object> result = new HashMap<>();
                result.put("Email",Email);
                result.put("Name",Name);
                result.put("Mobile_number",Phone);
                result.put("Gender",Gender);
                result.put("Job_title",Jobtitle);

                databaseReference.updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(profile_page.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profile_page.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });





        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Email = String.valueOf(dataSnapshot.child("Email").getValue());
                String Phone = String.valueOf(dataSnapshot.child("Mobile_number").getValue());
                String Name = String.valueOf(dataSnapshot.child("Name").getValue());
                String Jobtitle = String.valueOf(dataSnapshot.child("Job_title").getValue());
                String Gender = String.valueOf(dataSnapshot.child("Gender").getValue());

                et_email.setText(Email);
                et_name.setText(Name);
                et_phone.setText(Phone);
                et_jobtitle.setText(Jobtitle);
                if(TextUtils.equals(Gender,rdm.getText().toString()))
                {
                    rdm.setChecked(true);
                }
                else if(TextUtils.equals(Gender,rdf.getText().toString()))
                {
                    rdf.setChecked(true);
                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
