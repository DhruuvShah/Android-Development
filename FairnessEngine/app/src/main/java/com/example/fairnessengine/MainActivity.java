package com.example.fairnessengine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        View logoContainer = findViewById(R.id.logo_container);
        logoContainer.setAlpha(0f);
        logoContainer.setScaleX(0.5f);
        logoContainer.setScaleY(0.5f);
        
        logoContainer.animate()
            .alpha(1f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(1000)
            .setInterpolator(new OvershootInterpolator())
            .withEndAction(() -> {
                logoContainer.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(300)
                    .start();
            })
            .start();

        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            // Artificial delay for splash effect
            try { Thread.sleep(2500); } catch (InterruptedException ignored) {}
            
            int count = app.getDatabase().appDao().getAllRoommatesSync().size();
            runOnUiThread(() -> {
                Intent intent;
                if (count == 0) {
                    intent = new Intent(this, AddRoommatesActivity.class);
                } else {
                    intent = new Intent(this, HomeDashboardActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        });
    }
}
