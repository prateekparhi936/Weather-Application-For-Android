package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("splash screen activity started");
        setContentView(R.layout.activity_splash_screen);
        showviews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideviews();
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 4000);
        showviews();
    }
    public void hideviews(){
        ImageView iv1=(ImageView)findViewById(R.id.splash_logo_mid);
        ImageView iv2=(ImageView)findViewById(R.id.splash_logo_bot);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1_splash);
        TextView tv1=(TextView)findViewById(R.id.tv_below_prog);
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        bar1.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.VISIBLE);
    }

    public void showviews(){
        ImageView iv1=(ImageView)findViewById(R.id.splash_logo_mid);
        ImageView iv2=(ImageView)findViewById(R.id.splash_logo_bot);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1_splash);
        TextView tv1=(TextView)findViewById(R.id.tv_below_prog);
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.VISIBLE);
        bar1.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
    }
}