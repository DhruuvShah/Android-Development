package com.example.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvFullName = findViewById(R.id.tvFullName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvMobile = findViewById(R.id.tvMobile);

        Intent intent = getIntent();
        String fName = intent.getStringExtra("FIRST_NAME");
        String lName = intent.getStringExtra("LAST_NAME");
        String email = intent.getStringExtra("EMAIL");
        String mobile = intent.getStringExtra("MOBILE");

        if (fName == null) fName = "No First Name Provided";
        if (lName == null) lName = "No Last Name Provided";
        if (email == null) email = "No Email Provided";
        if (mobile == null) mobile = "No Mobile Provided";

        tvFullName.setText(fName + " " + lName);
        tvEmail.setText(email);
        tvMobile.setText(mobile);
    }
}