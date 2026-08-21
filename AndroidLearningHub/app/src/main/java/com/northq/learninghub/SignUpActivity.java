package com.northq.learninghub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout nameLayout, emailLayout, usernameLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText nameInput, emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private CheckBox termsCheck;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUpRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        nameLayout = findViewById(R.id.nameInputLayout);
        emailLayout = findViewById(R.id.emailInputLayout);
        usernameLayout = findViewById(R.id.usernameInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordInputLayout);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        termsCheck = findViewById(R.id.termsCheck);
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void setupListeners() {
        nameInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) nameLayout.setError(null);
            }
        });

        emailInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                boolean valid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
                emailLayout.setEndIconVisible(valid);
                if (valid) emailLayout.setError(null);
            }
        });

        usernameInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("[a-zA-Z0-9]*")) {
                    usernameLayout.setError(null);
                } else {
                    usernameLayout.setError("Username must only contain letters and numbers");
                }
            }
        });

        signUpButton.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        boolean isValid = true;

        if (TextUtils.isEmpty(nameInput.getText())) {
            nameLayout.setError("Name is required");
            isValid = false;
        }

        String email = emailInput.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            isValid = false;
        }

        if (TextUtils.isEmpty(usernameInput.getText())) {
            usernameLayout.setError("Username is required");
            isValid = false;
        }

        String password = passwordInput.getText().toString();
        if (password.length() < 6) {
            passwordLayout.setError("Password needs at least 6 characters");
            isValid = false;
        }

        if (!password.equals(confirmPasswordInput.getText().toString())) {
            confirmPasswordLayout.setError("Passwords do not match");
            isValid = false;
        }

        if (!termsCheck.isChecked()) {
            Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}
