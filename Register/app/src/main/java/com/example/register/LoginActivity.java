package com.example.register; // Ensure this matches your package name!

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLogEmail);
        etPassword = findViewById(R.id.etLogPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = etEmail.getText().toString().trim();
                String inputPass = etPassword.getText().toString().trim();

                if (inputEmail.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Both fields are compulsory!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent receivedIntent = getIntent();
                String registeredEmail = receivedIntent.getStringExtra("EMAIL");
                String registeredPass = receivedIntent.getStringExtra("PASSWORD");

                if (inputEmail.equals(registeredEmail) && inputPass.equals(registeredPass)) {
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    homeIntent.putExtra("FIRST_NAME", receivedIntent.getStringExtra("FIRST_NAME"));
                    homeIntent.putExtra("LAST_NAME", receivedIntent.getStringExtra("LAST_NAME"));
                    homeIntent.putExtra("EMAIL", registeredEmail);
                    homeIntent.putExtra("MOBILE", receivedIntent.getStringExtra("MOBILE"));

                    startActivity(homeIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}