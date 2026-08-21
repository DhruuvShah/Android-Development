package com.example.call_and_link;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ImplicitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit);

        // Declare the button variables mapped to the XML IDs
        Button buttonCall = findViewById(R.id.buttonCall);
        Button buttonLink = findViewById(R.id.buttonLink);

        buttonCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8799300885"));
            startActivity(callIntent);
        });

        buttonLink.setOnClickListener(view -> {
            Intent linkIntent = new Intent(Intent.ACTION_VIEW);
            linkIntent.setData(Uri.parse("https://www.google.com"));
            startActivity(linkIntent);
        });
    }
}