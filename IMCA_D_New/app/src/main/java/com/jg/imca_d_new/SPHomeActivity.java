package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SPHomeActivity extends AppCompatActivity {

    TextView txtTitle;
    Button btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sphome);

        txtTitle = findViewById(R.id.txtTitle);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("IMCAD",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();


        txtTitle.setText(sharedPreferences.getString("FNAME","not found"));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("FNAME");
                editor.remove("EMAIL");
                editor.remove("PASSWORD");
                editor.remove("IS_LOGIN");
                editor.clear();

                editor.apply();
                editor.commit();



                startActivity(new Intent(SPHomeActivity.this, SPLoginActivity.class));
                finishAffinity();
            }
        });
    }
}