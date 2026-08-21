package com.jg.imca_ddivision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.button);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onRestart","This method is called onRestart");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","This method is called onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart","This method is called onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop","This method is called onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","This method is called onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","This method is called onPause");
    }


}