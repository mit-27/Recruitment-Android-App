package com.example.recruitmentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_screen extends AppCompatActivity {

    TextInputEditText et_email,et_password,et_cpassword;
    Button btn_signup;
    // ProgressBar to display progress of sign up
    ProgressDialog progressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        // Action Bar and its Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_icon);




        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign Up...");

        // Handle the signup button

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String cpassword = et_cpassword.getText().toString().trim();


                if(!password.equals(cpassword))
                {
                    et_cpassword.setError("Password is not Matching");
                    et_cpassword.setFocusable(true);
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    et_email.setError("Invalid Email");
                    et_email.setFocusable(true);
                }
                else if(password.length()<4 || password.length()>10)
                {
                    et_password.setError("Between 4 to 10 alphanumeric Characters");
                    et_password.setFocusable(true);
                }
                else
                {
                    registeruser(email,password);
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void registeruser(String aemail, String password)
    {
        // Email and Password are valid , show progress dialogue and register User
        progressDialog.show();
        final String email=aemail;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialogue and start dashboard activity
                            progressDialog.dismiss();
                            final FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Signup_screen.this,
                                                        "Verification mail sent to your Email", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(),Login_screen.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(Signup_screen.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });




                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Signup_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Dismiss progrss Dialogue and show error msg
                        progressDialog.dismiss();
                        Toast.makeText(Signup_screen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        ;


    }



    public void login_page_open(View view)
    {
        Intent i = new Intent(getApplicationContext(),Login_screen.class);
        startActivity(i);
        finish();
    }
}
