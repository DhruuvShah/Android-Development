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

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtpassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass;

                email = edtEmail.getText().toString().trim();
                pass = edtPassword.getText().toString().trim();

                String spemail = sharedPreferences.getString("EMAIL", "not found");
                String spPass = sharedPreferences.getString("PASSWORD", "not found");

                if(email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_LONG).show();
                } else if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_LONG).show();
                } else {
                    if (email.equals(spemail) && pass.equals(spPass)) {
                        Toast.makeText(LoginActivity.this, "Login SuccessFull...!", Toast.LENGTH_LONG).show();
                        
                        editor.putBoolean("IS_LOGIN", true);
                        editor.apply();
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Your email and pass are invalid...!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
