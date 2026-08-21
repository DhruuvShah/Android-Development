package com.example.fairnessengine;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoommateDetailActivity extends AppCompatActivity {
    private int roommateId;
    private Roommate roommate;
    private PendingTasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_detail);

        roommateId = getIntent().getIntExtra("ROOMMATE_ID", -1);
        if (roommateId == -1) {
            finish();
            return;
        }

        findViewById(R.id.btn_close).setOnClickListener(v -> finish());

        RecyclerView recyclerPending = findViewById(R.id.recycler_pending_tasks);
        recyclerPending.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PendingTasksAdapter();
        recyclerPending.setAdapter(adapter);

        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            roommate = app.getDatabase().appDao().getRoommateSync(roommateId);
            if (roommate != null) {
                runOnUiThread(this::bindHeader);
            }
        });

        app.getDatabase().appDao().getPendingLogsForRoommate(roommateId).observe(this, logs -> {
            if (logs != null) {
                adapter.setLogs(logs);
                findViewById(R.id.tv_empty).setVisibility(logs.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void bindHeader() {
        TextView tvAvatar = findViewById(R.id.tv_avatar);
        TextView tvName = findViewById(R.id.tv_name);

        tvName.setText(roommate.name);
        if (!roommate.name.isEmpty()) {
            tvAvatar.setText(roommate.name.substring(0, 1).toUpperCase());
        }

        int color = Color.parseColor(roommate.colorHex != null ? roommate.colorHex : "#FF6B5B");
        GradientDrawable bg = (GradientDrawable) tvAvatar.getBackground().mutate();
        bg.setColor(color);
        tvName.setTextColor(color);
    }

    private static class PendingTasksAdapter extends RecyclerView.Adapter<PendingTasksAdapter.ViewHolder> {
        private List<AssignmentLog> logs = new ArrayList<>();
        private final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());

        public void setLogs(List<AssignmentLog> list) {
            this.logs = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_task, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AssignmentLog log = logs.get(position);
            holder.tvChoreName.setText(log.choreNameSnapshot);
            if (log.completed) {
                holder.btnComplete.setVisibility(android.view.View.GONE);
                holder.tvDate.setText("Completed: " + sdf.format(new Date(log.dateAssigned)));
            } else {
                holder.btnComplete.setVisibility(android.view.View.VISIBLE);
                holder.tvDate.setText("Assigned: " + sdf.format(new Date(log.dateAssigned)));
            }
            if (holder.ivIcon != null) {
                holder.ivIcon.setImageResource(ChoreIconUtil.getIconResId(log.choreNameSnapshot));
            }

            holder.btnComplete.setOnClickListener(v -> {
                FairnessEngineApp app = (FairnessEngineApp) v.getContext().getApplicationContext();
                app.executorService.execute(() -> {
                    
                    log.completed = true;
                    app.getDatabase().appDao().updateAssignmentLog(log);
                    
                    Roommate r = app.getDatabase().appDao().getRoommateSync(log.roommateId);
                    if (r != null) {
                        r.cumulativeEffort += 1;
                        app.getDatabase().appDao().updateRoommate(r);
                    }

                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> {
                        Toast.makeText(v.getContext(), "Marked as complete!", Toast.LENGTH_SHORT).show();
                        android.content.Intent notifIntent = new android.content.Intent(v.getContext(), NotificationReceiver.class);
                        notifIntent.putExtra("title", "Task Completed!");
                        notifIntent.putExtra("message", "Task \"" + log.choreNameSnapshot + "\" has been completed.");
                        v.getContext().sendBroadcast(notifIntent);
                        
                        // Update UI
                        log.completed = true;
                        notifyItemChanged(position);
                    });
                });
            });
        }

        @Override
        public int getItemCount() { return logs.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvChoreName, tvDate;
            android.widget.ImageView ivIcon;
            Button btnComplete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvChoreName = itemView.findViewById(R.id.tv_chore_name);
                tvDate = itemView.findViewById(R.id.tv_date);
                ivIcon = itemView.findViewById(R.id.iv_icon);
                btnComplete = itemView.findViewById(R.id.btn_complete_task);
            }
        }
    }
}
