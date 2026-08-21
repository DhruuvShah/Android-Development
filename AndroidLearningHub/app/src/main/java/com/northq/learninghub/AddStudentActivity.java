package com.northq.learninghub;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        
        findViewById(R.id.submitEnrollment).setOnClickListener(v -> {
            Toast.makeText(this, "Enrollment submitted successfully", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
