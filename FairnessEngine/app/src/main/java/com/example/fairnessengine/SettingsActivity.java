package com.example.fairnessengine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_reset_points).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Reset Fairness Scores")
                .setMessage("Are you sure you want to set everyone's points back to zero? This won't delete the history log.")
                .setPositiveButton("Reset", (dialog, which) -> resetPoints())
                .setNegativeButton("Cancel", null)
                .show();
        });

        findViewById(R.id.btn_clear_data).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("⚠️ DANGER: Clear All Data")
                .setMessage("This will delete all roommates, chores, and logs permanently. The app will restart.")
                .setPositiveButton("DELETE EVERYTHING", (dialog, which) -> clearAllData())
                .setNegativeButton("Cancel", null)
                .show();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_settings) return true;
            
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeDashboardActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_chores) {
                startActivity(new Intent(this, ChoresListActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_roommates) {
                Intent intent = new Intent(this, AddRoommatesActivity.class);
                intent.putExtra("manage_mode", true);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(this, HistoryLogActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void resetPoints() {
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            java.util.List<Roommate> roommates = dao.getAllRoommatesSync();
            for (Roommate r : roommates) {
                r.cumulativeEffort = 0;
                dao.updateRoommate(r);
            }
            runOnUiThread(() -> Toast.makeText(this, "Scores reset successfully", Toast.LENGTH_SHORT).show());
        });
    }

    private void clearAllData() {
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            app.getDatabase().clearAllTables();
            runOnUiThread(() -> {
                Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}
