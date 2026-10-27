package com.example.fragmentsapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FragmentActivity extends AppCompatActivity {
    ImageView img1, img2, img3;

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentHomeActivity()).commit();

        if (img1 != null) {
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change(new FragmentHomeActivity());
                }
            });
        }

        if (img2 != null) {
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change(new FragmentReelActivity());
                }
            });
        }

        if (img3 != null) {
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change(new FragmentSendActivity());
                }
            });
        }
    }

    public void change(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}
