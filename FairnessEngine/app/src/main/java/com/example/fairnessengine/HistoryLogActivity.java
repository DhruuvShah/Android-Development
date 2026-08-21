package com.example.fairnessengine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryLogActivity extends AppCompatActivity {
    private RecyclerView recyclerHistory;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_history);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) return true;
            
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
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });

        recyclerHistory = findViewById(R.id.recycler_history);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        recyclerHistory.setAdapter(adapter);

        AppDatabase db = ((FairnessEngineApp) getApplication()).getDatabase();
        db.appDao().getAllAssignmentLogs().observe(this, logs -> {
            if (logs != null) {
                adapter.setLogs(logs);
                if (logs.isEmpty()) {
                    findViewById(R.id.empty_state).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.empty_state).setVisibility(View.GONE);
                }
            }
        });
    }
    
    private static class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        
        private List<Object> displayList = new ArrayList<>();
        private final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM", Locale.US);
        private final SimpleDateFormat shortDay = new SimpleDateFormat("EEE", Locale.US);
        
        public void setLogs(List<AssignmentLog> list) {
            displayList.clear();
            
            boolean hasThisWeek = false;
            boolean hasLastWeek = false;
            
            long now = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(now);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            long startOfWeek = cal.getTimeInMillis();
            
            List<AssignmentLog> thisWeekLogs = new ArrayList<>();
            List<AssignmentLog> pastLogs = new ArrayList<>();
            
            for (AssignmentLog log : list) {
                if (log.dateAssigned >= startOfWeek) {
                    thisWeekLogs.add(log);
                } else {
                    pastLogs.add(log);
                }
            }
            
            if (!thisWeekLogs.isEmpty()) {
                displayList.add("THIS WEEK");
                displayList.addAll(thisWeekLogs);
            }
            if (!pastLogs.isEmpty()) {
                displayList.add("LAST WEEK");
                displayList.addAll(pastLogs);
            }
            
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (displayList.get(position) instanceof String) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_header, parent, false);
                return new HeaderViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
                return new ItemViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).tvHeader.setText((String) displayList.get(position));
            } else if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
                AssignmentLog log = (AssignmentLog) displayList.get(position);
                
                itemHolder.tvName.setText(log.roommateNameSnapshot);
                itemHolder.tvChore.setText(log.choreNameSnapshot);
                
                int choreIconRes = log.choreIconSnapshot != null ? 
                    ChoreIconUtil.getIconResId(log.choreIconSnapshot) : 
                    ChoreIconUtil.guessIconResId(log.choreNameSnapshot);
                itemHolder.ivChoreIcon.setImageResource(choreIconRes);
                int iconColor = ChoreIconUtil.getIconColor(log.choreIconSnapshot != null ? log.choreIconSnapshot : "ic_other");
                itemHolder.ivChoreIcon.setColorFilter(iconColor);

                long now = System.currentTimeMillis();
                long diff = now - log.dateAssigned;
                if (diff < 86400000) {
                    itemHolder.tvDate.setText("Today");
                } else if (diff < 86400000 * 2) {
                    itemHolder.tvDate.setText("Yesterday");
                } else if (diff < 86400000 * 7) {
                    itemHolder.tvDate.setText(shortDay.format(new Date(log.dateAssigned)));
                } else {
                    itemHolder.tvDate.setText(sdf.format(new Date(log.dateAssigned)));
                }
                
                int color = Color.parseColor(log.roommateColorSnapshot != null ? log.roommateColorSnapshot : "#56C2E0");
                itemHolder.tvName.setTextColor(color);
                itemHolder.timelineDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
                
                if (log.completed) {
                    itemHolder.ivStatus.setImageResource(R.drawable.ic_check_circle);
                    itemHolder.tvChore.setTextColor(Color.WHITE);
                    itemHolder.tvChore.setPaintFlags(itemHolder.tvChore.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else if (log.skipped) {
                    itemHolder.ivStatus.setImageResource(R.drawable.ic_dash);
                    itemHolder.tvChore.setTextColor(Color.parseColor("#A8949E"));
                    itemHolder.tvChore.setPaintFlags(itemHolder.tvChore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    // Pending/in-progress case (not defined in requirements but safety net)
                    itemHolder.ivStatus.setImageDrawable(null);
                    itemHolder.tvChore.setTextColor(Color.WHITE);
                    itemHolder.tvChore.setPaintFlags(itemHolder.tvChore.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                
                // Hide timeline line if it's the last item
                if (position == displayList.size() - 1 || getItemViewType(position + 1) == TYPE_HEADER) {
                    itemHolder.timelineLine.setVisibility(View.INVISIBLE);
                } else {
                    itemHolder.timelineLine.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() { return displayList.size(); }
        
        static class HeaderViewHolder extends RecyclerView.ViewHolder {
            TextView tvHeader;
            public HeaderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvHeader = itemView.findViewById(R.id.tv_header);
            }
        }
        
        static class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDate, tvChore;
            ImageView ivStatus, ivChoreIcon;
            View timelineLine, timelineDot;
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvChore = itemView.findViewById(R.id.tv_chore);
                ivStatus = itemView.findViewById(R.id.iv_status);
                ivChoreIcon = itemView.findViewById(R.id.iv_chore_icon);
                timelineLine = itemView.findViewById(R.id.timeline_line);
                timelineDot = itemView.findViewById(R.id.timeline_dot);
            }
        }
    }
}
