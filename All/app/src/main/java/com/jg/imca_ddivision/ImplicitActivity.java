package com.jg.imca_ddivision;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ImplicitActivity extends AppCompatActivity {

    Button buttonCall,buttonLink, buttonBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_implicit);

        buttonCall = findViewById(R.id.btnCall);
        buttonLink = findViewById(R.id.btnLink);
        buttonBack = findViewById(R.id.btnBack);

        buttonCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:0987654321"));
            startActivity(callIntent);
        });

        buttonLink.setOnClickListener(view ->{
            Intent linkIntent = new Intent(Intent.ACTION_VIEW);
            linkIntent.setData(Uri.parse("https://openai.com/index/chatgpt/"));
            startActivity(linkIntent);
        });

        buttonBack.setOnClickListener(view -> {
            finish();
        });

    }
}