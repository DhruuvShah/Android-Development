package com.example.sharedpreferenceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash);

        View splashContent = findViewById(R.id.splashContent);
        splashContent.setAlpha(0f);
        splashContent.setScaleX(0.8f);
        splashContent.setScaleY(0.8f);

        splashContent.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .start();

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("IS_LOGIN", false))
                {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();  
            }
        }, 3000); 
    }
}
