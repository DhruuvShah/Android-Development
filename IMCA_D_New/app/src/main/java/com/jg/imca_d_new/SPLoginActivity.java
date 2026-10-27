package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SPLoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnSignIn,btnSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splogin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtpassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email,pass;

                email = edtEmail.getText().toString();
                pass = edtPassword.getText().toString();

                String spemail = sharedPreferences.getString("EMAIL","not found");
                String spPass = sharedPreferences.getString("PASSWORD","not found");

                if(email.isEmpty())
                {
                    Toast.makeText(SPLoginActivity.this,"Enter email",Toast.LENGTH_LONG).show();
                } else if (pass.isEmpty()) {
                    Toast.makeText(SPLoginActivity.this,"Enter password",Toast.LENGTH_LONG).show();
                }else {
                    if (email.equals(spemail) && pass.equals(spPass)) {
                        Toast.makeText(SPLoginActivity.this,"Login SuccessFull...!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SPLoginActivity.this,SPHomeActivity.class));
                        finish();

                        editor.putBoolean("IS_LOGIN",true);
                        editor.apply();
                        editor.commit();

                    }else{
                        Toast.makeText(SPLoginActivity.this,"Your email and pass are invalid...!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loginIntent;
                loginIntent = new Intent(SPLoginActivity.this, SPRegisterActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}