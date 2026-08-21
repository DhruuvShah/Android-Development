package com.northq.learninghub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "hub_prefs";
    public static final String KEY_REMEMBER = "remember_me";
    public static final String KEY_EMAIL = "saved_email";

    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private CheckBox rememberMeCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberMeCheck = findViewById(R.id.rememberMeCheck);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_REMEMBER, false)) {
            emailInput.setText(prefs.getString(KEY_EMAIL, ""));
            rememberMeCheck.setChecked(true);
        }

        findViewById(R.id.loginButton).setOnClickListener(v -> attemptLogin());
        
        findViewById(R.id.goToSignUp).setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
                
        findViewById(R.id.forgotPassword).setOnClickListener(v -> 
                Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show());

        findViewById(R.id.guestButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, HubActivity.class);
            intent.putExtra("user_email", "Guest User");
            startActivity(intent);
            finish();
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            return;
        }
        if (password.length() < 4) {
            passwordLayout.setError("Password must be at least 4 characters");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_REMEMBER, rememberMeCheck.isChecked());
        editor.putString(KEY_EMAIL, rememberMeCheck.isChecked() ? email : "");
        editor.apply();

        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra("user_email", email);
        startActivity(intent);
        finish();
    }
}
