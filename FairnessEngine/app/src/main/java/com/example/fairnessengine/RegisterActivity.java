package com.example.fairnessengine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editName = findViewById(R.id.edit_full_name);
        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPassword = findViewById(R.id.edit_password);
        Button btnSubmit = findViewById(R.id.btn_register);

        btnSubmit.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("registered_email", email);
            resultIntent.putExtra("registered_password", password);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
