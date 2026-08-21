package com.example.fairnessengine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeDashboardActivity extends AppCompatActivity {
    private RoommateBalanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            
            Intent intent = null;
            if (id == R.id.nav_history) {
                intent = new Intent(this, HistoryLogActivity.class);
            } else if (id == R.id.nav_chores) {
                intent = new Intent(this, ChoresListActivity.class);
            } else if (id == R.id.nav_roommates) {
                intent = new Intent(this, AddRoommatesActivity.class);
                intent.putExtra("manage_mode", true);
            } else if (id == R.id.nav_settings) {
                intent = new Intent(this, SettingsActivity.class);
            }
            
            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });

        RecyclerView recyclerRoommates = findViewById(R.id.recycler_roommates);
        recyclerRoommates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoommateBalanceAdapter();
        recyclerRoommates.setAdapter(adapter);

        findViewById(R.id.fab_assign_chore).setOnClickListener(v -> {
            FairnessEngineApp app = (FairnessEngineApp) getApplication();
            app.executorService.execute(() -> {
                AppDao dao = app.getDatabase().appDao();
                List<Chore> chores = dao.getAllChoresSync();
                if (chores.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Add a chore first", Toast.LENGTH_SHORT).show());
                } else {
                    List<Roommate> roommates = dao.getAllRoommatesSync();
                    List<AssignmentLog> history = dao.getAllAssignmentLogsSync();
                    Roommate assignee = FairnessAllocator.getNextAssignee(roommates, history);
                    
                    if (assignee != null) {
                        Intent intent = new Intent(this, AssignChoreActivity.class);
                        intent.putExtra("ROOMMATE_ID", assignee.id);
                        intent.putExtra("CHORE_ID", chores.get(0).id);
                        startActivity(intent);
                    }
                }
            });
        });

        AppDatabase db = ((FairnessEngineApp) getApplication()).getDatabase();
        PieChartView pieChart = findViewById(R.id.pie_chart);
        
        db.appDao().getAllRoommates().observe(this, roommates -> {
            if (roommates != null) {
                if (roommates.isEmpty()) {
                    findViewById(R.id.card_balance).setVisibility(View.GONE);
                    findViewById(R.id.empty_state).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.card_balance).setVisibility(View.VISIBLE);
                    findViewById(R.id.empty_state).setVisibility(View.GONE);
                    
                    // Fetch metadata for intensity colors
                    FairnessEngineApp app = (FairnessEngineApp) getApplication();
                    app.executorService.execute(() -> {
                        AppDao dao = app.getDatabase().appDao();
                        List<AssignmentLog> pendingLogs = dao.getAllPendingLogsSync();
                        List<Chore> allChores = dao.getAllChoresSync();
                        
                        Map<Integer, Double> choreWeights = new HashMap<>();
                        for (Chore c : allChores) choreWeights.put(c.id, c.effortWeight);
                        
                        Map<Integer, Double> roommateMaxPendingEffort = new HashMap<>();
                        for (AssignmentLog log : pendingLogs) {
                            Double weightObj = choreWeights.get(log.choreId);
                            double weight = (weightObj != null) ? weightObj : 1.0;
                            
                            Double currentMaxObj = roommateMaxPendingEffort.get(log.roommateId);
                            double currentMax = (currentMaxObj != null) ? currentMaxObj : 0.0;
                            
                            if (weight > currentMax) roommateMaxPendingEffort.put(log.roommateId, weight);
                        }

                        runOnUiThread(() -> {
                            adapter.setData(roommates, roommateMaxPendingEffort);
                            
                            List<Float> values = new ArrayList<>();
                            List<Integer> colors = new ArrayList<>();
                                        float totalEffort = 0;
                            for (Roommate r : roommates) {
                                values.add((float) r.cumulativeEffort);
                                totalEffort += (float) r.cumulativeEffort;
                                colors.add(Color.parseColor(r.colorHex != null ? r.colorHex : "#FF6B5B"));
                            }
                            pieChart.setData(values, colors);
                            pieChart.setCenterText("" + (int) totalEffort);
                        });
                    });
                }
            }
        });
    }

    private static class RoommateBalanceAdapter extends RecyclerView.Adapter<RoommateBalanceAdapter.ViewHolder> {
        private List<Roommate> roommates = new ArrayList<>();
        private Map<Integer, Double> intensityMap = new HashMap<>();
        private double totalEffort = 1;
        
        public void setData(List<Roommate> list, Map<Integer, Double> intensityMap) {
            this.roommates = list;
            this.intensityMap = intensityMap;
            this.totalEffort = 0;
            for (Roommate r : list) {
                totalEffort += r.cumulativeEffort;
            }
            if (totalEffort == 0) totalEffort = 1;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roommate_home, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Roommate r = roommates.get(position);
            holder.tvName.setText(r.name);
            holder.tvPoints.setText(String.valueOf((int) r.cumulativeEffort));
            if (!r.name.isEmpty()) {
                holder.tvAvatar.setText(r.name.substring(0, 1).toUpperCase());
            }
            
            int identityColor = Color.parseColor(r.colorHex != null ? r.colorHex : "#FF6B5B");
            ((GradientDrawable) holder.tvAvatar.getBackground().mutate()).setColor(identityColor);
            
            // Set Progress Bar Color based on intensity of pending task
            Double intensityObj = intensityMap.get(r.id);
            double maxPendingWeight = (intensityObj != null) ? intensityObj : 0.0;
            int barColor = (maxPendingWeight > 0) ? ChoreIconUtil.getEffortColor(maxPendingWeight) : identityColor;
            ((GradientDrawable) holder.barFill.getBackground().mutate()).setColor(barColor);
            
            // Interconnected progress: relative to total household load
            float percentage = (float) (r.cumulativeEffort / totalEffort);
            
            holder.barBg.post(() -> {
                int availableWidth = holder.barBg.getWidth();
                ViewGroup.LayoutParams p = holder.barFill.getLayoutParams();
                p.width = (int) (availableWidth * percentage);
                if (p.width < 10 && percentage > 0.01) p.width = 10;
                holder.barFill.setLayoutParams(p);
            });

            holder.tvUpNext.setVisibility((position == roommates.size() - 1 && !roommates.isEmpty()) ? View.VISIBLE : View.GONE);
            
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), RoommateDetailActivity.class);
                intent.putExtra("ROOMMATE_ID", r.id);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return roommates.size(); }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvAvatar, tvName, tvPoints, tvUpNext;
            View barBg, barFill;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAvatar = itemView.findViewById(R.id.tv_avatar);
                tvName = itemView.findViewById(R.id.tv_name);
                tvPoints = itemView.findViewById(R.id.tv_score);
                tvUpNext = itemView.findViewById(R.id.tv_up_next);
                barBg = itemView.findViewById(R.id.bar_track);
                barFill = itemView.findViewById(R.id.bar_fill);
            }
        }
    }
}
