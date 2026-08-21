package com.example.fairnessengine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AssignChoreActivity extends AppCompatActivity {
    private int choreId;
    private int excludedRoommateId = -1;
    private Chore chore;
    private Roommate assignee;
    private boolean wasManualOverride = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chore);

        choreId = getIntent().getIntExtra("CHORE_ID", -1);
        excludedRoommateId = getIntent().getIntExtra("ROOMMATE_ID", -1);

        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { finish(); }
        });

        findViewById(R.id.chore_selection_container).setOnClickListener(v -> showChorePicker());

        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            if (choreId != -1) chore = dao.getChoreSync(choreId);
            if (chore != null) {
                List<Roommate> roommates = dao.getAllRoommatesSync();
                List<AssignmentLog> history = dao.getAllAssignmentLogsSync();
                assignee = FairnessAllocator.getNextAssigneeForChore(roommates, history, chore, excludedRoommateId);
                if (assignee != null) {
                    excludedRoommateId = assignee.id;
                    runOnUiThread(this::bindUI);
                }
            }
        });

        findViewById(R.id.btn_reassign).setOnClickListener(v -> {
            app.executorService.execute(() -> {
                AppDao dao = app.getDatabase().appDao();
                List<Roommate> roommates = dao.getAllRoommatesSync();
                List<AssignmentLog> history = dao.getAllAssignmentLogsSync();
                assignee = FairnessAllocator.getNextAssigneeForChore(roommates, history, chore, excludedRoommateId);
                if (assignee != null) {
                    excludedRoommateId = assignee.id;
                    wasManualOverride = false;
                    runOnUiThread(this::bindUI);
                }
            });
        });

        findViewById(R.id.btn_pick_other).setOnClickListener(v -> {
            app.executorService.execute(() -> {
                AppDao dao = app.getDatabase().appDao();
                List<Roommate> roommates = dao.getAllRoommatesSync();
                runOnUiThread(() -> {
                    String[] items = new String[roommates.size()];
                    for (int i = 0; i < roommates.size(); i++) {
                        Roommate r = roommates.get(i);
                        items[i] = r.name + " (" + (int)r.cumulativeEffort + " pts)";
                    }
                    new AlertDialog.Builder(this)
                        .setTitle("Pick someone else")
                        .setItems(items, (dialog, which) -> {
                            assignee = roommates.get(which);
                            excludedRoommateId = assignee.id;
                            wasManualOverride = true;
                            bindUI();
                        })
                        .show();
                });
            });
        });

        findViewById(R.id.btn_complete).setOnClickListener(v -> saveAssignment());
    }

    private void showChorePicker() {
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            List<Chore> chores = app.getDatabase().appDao().getAllChoresSync();
            runOnUiThread(() -> {
                String[] names = new String[chores.size()];
                for (int i = 0; i < chores.size(); i++) names[i] = chores.get(i).name;
                new AlertDialog.Builder(this)
                    .setTitle("Select Chore")
                    .setItems(names, (dialog, which) -> {
                        chore = chores.get(which);
                        choreId = chore.id;
                        recalculateAssignee();
                    })
                    .show();
            });
        });
    }

    private void recalculateAssignee() {
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            List<Roommate> roommates = dao.getAllRoommatesSync();
            List<AssignmentLog> history = dao.getAllAssignmentLogsSync();
            assignee = FairnessAllocator.getNextAssigneeForChore(roommates, history, chore, -1);
            if (assignee != null) {
                excludedRoommateId = assignee.id;
                wasManualOverride = false;
                runOnUiThread(this::bindUI);
            }
        });
    }

    private void saveAssignment() {
        if (chore == null || assignee == null) return;
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            AssignmentLog log = new AssignmentLog();
            log.choreId = chore.id;
            log.roommateId = assignee.id;
            log.roommateNameSnapshot = assignee.name;
            log.roommateColorSnapshot = assignee.colorHex;
            log.choreNameSnapshot = chore.name;
            log.choreIconSnapshot = chore.iconName;
            log.dateAssigned = System.currentTimeMillis();
            log.completed = false;
            log.wasManualOverride = wasManualOverride;
            dao.insertAssignmentLog(log);

            try {
                Intent notifIntent = new Intent(this, NotificationReceiver.class);
                notifIntent.putExtra("title", "Task Assigned");
                notifIntent.putExtra("message", assignee.name + ", you have a pending task: '" + chore.name + "'.");
                sendBroadcast(notifIntent);
            } catch (Exception ignored) {}

            runOnUiThread(() -> {
                Intent intent = new Intent(this, HomeDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        });
    }

    private void bindUI() {
        TextView tvAvatar = findViewById(R.id.tv_avatar);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvChoreName = findViewById(R.id.tv_chore_name);
        TextView tvReason = findViewById(R.id.tv_reason);
        Button btnAssign = findViewById(R.id.btn_complete);
        View bgGlow = findViewById(R.id.bg_glow);
        ImageView ivChoreIcon = findViewById(R.id.iv_chore_icon);
        
        tvChoreName.setText(chore.name);
        int iconRes = chore.iconName != null ? ChoreIconUtil.getIconResId(chore.iconName) : ChoreIconUtil.guessIconResId(chore.name);
        ivChoreIcon.setImageResource(iconRes);
        int iconColor = ChoreIconUtil.getIconColor(chore.iconName != null ? chore.iconName : "ic_other");
        ivChoreIcon.setColorFilter(iconColor);

        tvName.setText(assignee.name);
        if (!assignee.name.isEmpty()) tvAvatar.setText(assignee.name.substring(0, 1).toUpperCase());
        
        int color = Color.parseColor(assignee.colorHex != null ? assignee.colorHex : "#56C2E0");
        ((GradientDrawable) tvAvatar.getBackground().mutate()).setColor(color);
        ((GradientDrawable) bgGlow.getBackground().mutate()).setColors(new int[]{ Color.argb(64, Color.red(color), Color.green(color), Color.blue(color)), Color.TRANSPARENT });
        
        tvName.setTextColor(color);
        btnAssign.setBackgroundColor(color);
        btnAssign.setText("Assign");
        tvReason.setText(String.format("Assigned because %1$s has the lightest load (%2$d pts)", assignee.name, (int) assignee.cumulativeEffort));
    }
}
