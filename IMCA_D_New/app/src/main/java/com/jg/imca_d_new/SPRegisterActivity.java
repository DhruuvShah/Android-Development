package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SPRegisterActivity extends AppCompatActivity {

    EditText edtfname,edtlname,edtemail,edtpassword;
    Button btnSubmit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spregister);


        edtfname = findViewById(R.id.edtFirstname);
        edtlname = findViewById(R.id.edtLastname);
        edtemail = findViewById(R.id.edtEmail);
        edtpassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtfname.getText().toString().isEmpty()){
                    Toast.makeText(SPRegisterActivity.this,"please enter your first name",Toast.LENGTH_LONG).show();
                } else if(edtlname.getText().toString().isEmpty()){
                    Toast.makeText(SPRegisterActivity.this,"please enter your last name",Toast.LENGTH_LONG).show();
                } else if (edtemail.getText().toString().isEmpty()) {
                    Toast.makeText(SPRegisterActivity.this,"please enter your email",Toast.LENGTH_LONG).show();
                } else if (edtpassword.getText().toString().isEmpty()) {
                    Toast.makeText(SPRegisterActivity.this,"please enter your password",Toast.LENGTH_LONG).show();
                }else{

                    editor.putString("FNAME",edtfname.getText().toString());
                    editor.putString("EMAIL",edtemail.getText().toString());
                    editor.putString("PASSWORD",edtpassword.getText().toString());

                    editor.apply();
                    editor.commit();


                    System.out.println(sharedPreferences.getString("FNAME","not found"));
                    System.out.println(sharedPreferences.getString("EMAIL","not found"));
                    System.out.println(sharedPreferences.getString("PASSWORD","not found"));


                    startActivity(new Intent(SPRegisterActivity.this, SPLoginActivity.class));
                    finish();
                }



            }
        });


    }
}