package com.example.recruitmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private ViewPager slideviewpager;
    private LinearLayout dots_id;
    private TextView[] dots;
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private Button login_btn,signup_btn;
    private  SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        slideviewpager = (ViewPager)findViewById(R.id.viewpager_id);
        dots_id = (LinearLayout)findViewById(R.id.dots_id);
        sliderAdapter = new SliderAdapter(this);
        slideviewpager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideviewpager.addOnPageChangeListener(viewListener);
        login_btn = (Button)findViewById(R.id.loginBtn);
        signup_btn = (Button)findViewById(R.id.signup);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Login_screen.class);
                startActivity(i);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Signup_screen.class);
                startActivity(i);
            }
        });
    }

    private void checkuserstatus()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null && user.isEmailVerified())
        {
            // User signed and go to dashboard
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();

        }
        else
        {
            // user is not signed and go to Mainactivity

        }
    }

    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    public void addDotsIndicator(int position)
    {
        dots = new TextView[2];
        dots_id.removeAllViews();

        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorWhite));

            dots_id.addView(dots[i]);
        }

        if(dots.length >0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.darkblue));
        }
    }
}
