package com.northq.learninghub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LifecycleActivity extends AppCompatActivity {

    private LinearLayout logContainer;
    private TextView currentStatePill;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        
        logContainer = findViewById(R.id.logContainer);
        currentStatePill = findViewById(R.id.currentStatePill);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnRotate).setOnClickListener(v -> 
                Toast.makeText(this, "Rotate device to trigger lifecycle changes", Toast.LENGTH_SHORT).show());
        
        findViewById(R.id.btnBackground).setOnClickListener(v -> 
                Toast.makeText(this, "Press Home to background activity", Toast.LENGTH_SHORT).show());

        addLog("onCreate");
    }

    private void addLog(String event) {
        if (currentStatePill != null) currentStatePill.setText(event);
        
        if (logContainer != null) {
            View logItem = LayoutInflater.from(this).inflate(R.layout.item_log, logContainer, false);
            TextView tv = logItem.findViewById(R.id.logText);
            String text = timeFormat.format(new Date()) + " - " + event;
            tv.setText(text);
            logContainer.addView(logItem, 0);
        }
    }

    @Override protected void onStart() { super.onStart(); addLog("onStart"); }
    @Override protected void onResume() { super.onResume(); addLog("onResume"); }
    @Override protected void onPause() { super.onPause(); addLog("onPause"); }
    @Override protected void onStop() { super.onStop(); addLog("onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); addLog("onDestroy"); }
    @Override protected void onRestart() { super.onRestart(); addLog("onRestart"); }
}
