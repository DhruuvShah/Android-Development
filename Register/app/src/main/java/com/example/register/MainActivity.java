package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etEmail, etPassword, etMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFirstName = findViewById(R.id.etRegFirstName);
        etLastName = findViewById(R.id.etRegLastName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etMobile = findViewById(R.id.etRegMobile);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFirstName.getText().toString().trim();
                String lName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();

                if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || pass.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are compulsory!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("FIRST_NAME", fName);
                    intent.putExtra("LAST_NAME", lName);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("PASSWORD", pass);
                    intent.putExtra("MOBILE", mobile);

                    Toast.makeText(MainActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}