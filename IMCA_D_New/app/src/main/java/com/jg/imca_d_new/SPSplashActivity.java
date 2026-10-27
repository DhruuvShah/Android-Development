package com.jg.imca_d_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SPSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spsplash);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("IS_LOGIN",false) == true)
                {
                    startActivity(new Intent(SPSplashActivity.this, SPHomeActivity.class));
                }else{
                    startActivity(new Intent(SPSplashActivity.this, SPLoginActivity.class));
                }
                finish();  // destroy single activity
            }
        },3000); // 3000 means 3 sec



















    }
}