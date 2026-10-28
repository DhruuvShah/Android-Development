package com.example.sharedpreferenceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtfname, edtlname, edtemail, edtpassword;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_register);

        edtfname = findViewById(R.id.edtFirstname);
        edtlname = findViewById(R.id.edtLastname);
        edtemail = findViewById(R.id.edtEmail);
        edtpassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtfname.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_LONG).show();
                } else if(edtlname.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your last name", Toast.LENGTH_LONG).show();
                } else if (edtemail.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                } else if (edtpassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                } else {
                    editor.putString("FNAME", edtfname.getText().toString());
                    editor.putString("LNAME", edtlname.getText().toString());
                    editor.putString("EMAIL", edtemail.getText().toString());
                    editor.putString("PASSWORD", edtpassword.getText().toString());

                    editor.apply();
                    editor.commit();

                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
