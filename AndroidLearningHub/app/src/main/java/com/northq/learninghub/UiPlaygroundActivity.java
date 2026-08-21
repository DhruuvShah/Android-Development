package com.northq.learninghub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UiPlaygroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_playground);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        EditText input = findViewById(R.id.pgInput);
        TextView preview = findViewById(R.id.pgPreview);

        input.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                preview.setText(s.length() > 0 ? s : "Live preview text");
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
