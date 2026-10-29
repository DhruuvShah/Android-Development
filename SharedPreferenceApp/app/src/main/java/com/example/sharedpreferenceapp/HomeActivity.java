package com.example.sharedpreferenceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView txtTitle;
    Button btnLogout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home);

        txtTitle = findViewById(R.id.txtTitle);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String fName = sharedPreferences.getString("FNAME", "not found");
        String lName = sharedPreferences.getString("LNAME", "");
        txtTitle.setText(fName + " " + lName);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("FNAME");
                editor.remove("LNAME");
                editor.remove("EMAIL");
                editor.remove("PASSWORD");
                editor.remove("IS_LOGIN");
                editor.clear();

                editor.apply();
                editor.commit();

                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
    }
}
