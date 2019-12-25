package com.example.recruitmentapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_candidate extends AppCompatActivity {

    EditText et_name,et_email,et_phone,et_status,et_source,et_jobtitle;
    RadioButton rdm,rdf;
    Button bt_add_candidate;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        // Action Bar and its Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Candidate");
        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_icon);

        et_name = findViewById(R.id.Can_input_name);
        et_email = findViewById(R.id.Can_input_email);
        et_jobtitle = findViewById(R.id.Can_input_jobTitle);
        et_phone = findViewById(R.id.Can_input_phone);
        et_status = findViewById(R.id.Can_input_currentStatus);
        et_source = findViewById(R.id.Can_input_source);
        rdm = findViewById(R.id.Can_rdm);
        rdf = findViewById(R.id.Can_rdf);
        bt_add_candidate = findViewById(R.id.addCandidateBtn);

        bt_add_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String jobtitle = et_jobtitle.getText().toString();
                String phone = et_phone.getText().toString();
                String status = et_status.getText().toString();
                String source = et_source.getText().toString();
                String gender="";
                if(rdm.isChecked())
                {
                    gender = rdm.getText().toString();
                }
                else if(rdf.isChecked())
                {
                    gender = rdf.getText().toString();
                }
                else
                {
                    rdm.setError("Select Gender");
//                    Toast.makeText(add_candidate.this, "Please Select Gender", Toast.LENGTH_SHORT).show();

                }

                if(TextUtils.isEmpty(name))
                {
                    et_name.setError("Please fill the Name");
                    et_name.setFocusable(true);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    et_email.setError("Invalid Email");
                    et_email.setFocusable(true);
                }
                else if(TextUtils.isEmpty(jobtitle))
                {
                    et_jobtitle.setError("Please fill the Job title");
                    et_jobtitle.setFocusable(true);
                }
                else if(TextUtils.isEmpty(phone))
                {
                    et_phone.setError("Please fill the Mobile Number");
                    et_phone.setFocusable(true);
                }
                else if(TextUtils.isEmpty(status))
                {
                    et_status.setError("Please fill the Status");
                    et_status.setFocusable(true);
                }
                else if(TextUtils.isEmpty(source))
                {
                    et_source.setError("Please fill the Source");
                    et_source.setFocusable(true);
                }
                else
                {
                    Addcandidate(name,email,jobtitle,phone,status,source,gender);
                }
            }
        });
    }

    private void Addcandidate(String name, String email, String jobtitle, String phone, String status, String source, String gender)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("Candidates_details");
        String id = databaseReference.push().getKey();
        boolean scheduled = false;
        Candidate candidate = new Candidate(name,email,gender,jobtitle,phone,source,status,id,scheduled);
        databaseReference.child(id).setValue(candidate);
        et_name.setText("");
        et_email.setText("");
        et_jobtitle.setText("");
        et_phone.setText("");
        et_status.setText("");
        et_source.setText("");




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
