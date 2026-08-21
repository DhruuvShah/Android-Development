package com.jg.imca_ddivision;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LinearLayoutActivity extends AppCompatActivity {

    Button buttonLogin, buttonBack;
    EditText editTextEmail,editTextPass;

    String email = "admin@gmail.com";
    String pass = "admin";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_linear_layout);


        buttonLogin = findViewById(R.id.btnLogin);
        buttonBack = findViewById(R.id.btnBack);
        editTextEmail = findViewById(R.id.edtEmail);
        editTextPass = findViewById(R.id.edtPass);

        buttonLogin.setOnClickListener(view ->{

            String name = editTextEmail.getText().toString();
            String password = editTextPass.getText().toString();

            Log.d("this is value of email",name);
            Log.d("this is value of pass",name);

            if(name.equals(email) && password.equals(pass))
            {
                Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show();

                Intent intent;
                intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(this, "Login fail", Toast.LENGTH_LONG).show();
            }
          //  Toast.makeText(this,name,Toast.LENGTH_LONG).show();

        });

        buttonBack.setOnClickListener(v -> finish());

    }
}