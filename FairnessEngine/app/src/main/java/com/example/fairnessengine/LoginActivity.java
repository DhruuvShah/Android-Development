package com.example.fairnessengine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private String registeredEmail = "";
    private String registeredPassword = "";

    private final ActivityResultLauncher<Intent> registerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    registeredEmail = result.getData().getStringExtra("registered_email");
                    registeredPassword = result.getData().getStringExtra("registered_password");
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPassword = findViewById(R.id.edit_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView btnGoToRegister = findViewById(R.id.btn_go_to_register);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
                Intent intent = new Intent(this, AddRoommatesActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials or not registered", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            registerLauncher.launch(intent);
        });
    }
}
