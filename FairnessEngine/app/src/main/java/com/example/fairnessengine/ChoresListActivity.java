package com.example.fairnessengine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class ChoresListActivity extends AppCompatActivity {
    private RecyclerView recyclerChores;
    private ChoresListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores_list);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        
        recyclerChores = findViewById(R.id.recycler_chores);
        recyclerChores.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChoresListAdapter();
        recyclerChores.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Chore c = adapter.getChoreAt(position);
                FairnessEngineApp app = (FairnessEngineApp) getApplication();
                if (swipeDir == ItemTouchHelper.LEFT) {
                    // Delete
                    app.executorService.execute(() -> {
                        app.getDatabase().appDao().deleteChore(c);
                        runOnUiThread(() -> {
                            Snackbar.make(recyclerChores, c.name + " deleted", Snackbar.LENGTH_SHORT).show();
                        });
                    });
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    // Complete (log it to the active roommate with least points)
                    app.executorService.execute(() -> {
                        AppDao dao = app.getDatabase().appDao();
                        java.util.List<Roommate> roommates = dao.getAllRoommatesSync();
                        java.util.List<AssignmentLog> history = dao.getAllAssignmentLogsSync();
                        Roommate assignee = FairnessAllocator.getNextAssignee(roommates, history);
                        if (assignee != null) {
                            AssignmentLog log = new AssignmentLog();
                            log.choreId = c.id;
                            log.roommateId = assignee.id;
                            log.roommateNameSnapshot = assignee.name;
                            log.roommateColorSnapshot = assignee.colorHex;
                            log.choreNameSnapshot = c.name;
                            log.choreIconSnapshot = c.iconName;
                            log.dateAssigned = System.currentTimeMillis();
                            log.completed = true;
                            log.wasManualOverride = false;
                            dao.insertAssignmentLog(log);
                            
                            assignee.cumulativeEffort += 1;
                            dao.updateRoommate(assignee);
                            
                            runOnUiThread(() -> {
                                adapter.notifyItemChanged(position); // to reset swipe view
                                Snackbar.make(recyclerChores, c.name + " completed by " + assignee.name, Snackbar.LENGTH_SHORT).show();
                                android.content.Intent notifIntent = new android.content.Intent(ChoresListActivity.this, NotificationReceiver.class);
                                notifIntent.putExtra("title", "Task Completed!");
                                notifIntent.putExtra("message", "Task \"" + c.name + "\" completed by " + assignee.name);
                                sendBroadcast(notifIntent);
                            });
                        }
                    });
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerChores);


        FloatingActionButton fabAdd = findViewById(R.id.fab_add_chore);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEditChoreActivity.class));
        });
        
        
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_chores);
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_chores) return true;
                
                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, HomeDashboardActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.nav_history) {
                    startActivity(new Intent(this, HistoryLogActivity.class));
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
        }
AppDatabase db = ((FairnessEngineApp) getApplication()).getDatabase();
        db.appDao().getAllChores().observe(this, chores -> {
            if (chores != null) {
                adapter.setChores(chores);
            }
        });
    }
    
    private static class ChoresListAdapter extends RecyclerView.Adapter<ChoresListAdapter.ViewHolder> {
        private List<Chore> chores = new ArrayList<>();
        
        
        public Chore getChoreAt(int position) {
            return chores.get(position);
        }

        public void setChores(List<Chore> list) {
            this.chores = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chore_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chore c = chores.get(position);
            holder.tvName.setText(c.name);
            holder.tvWeight.setText("" + (int) c.effortWeight);
            
            int iconRes = c.iconName != null ? ChoreIconUtil.getIconResId(c.iconName) : ChoreIconUtil.guessIconResId(c.name);
            holder.ivIcon.setImageResource(iconRes);
            int iconColor = ChoreIconUtil.getIconColor(c.iconName != null ? c.iconName : "ic_other");
            holder.ivIcon.setColorFilter(iconColor);
            
            int weight = (int) c.effortWeight;
            int color = Color.parseColor("#3C2F3A");
            if (weight == 2) color = Color.parseColor("#5A5C4F");
            else if (weight == 3) color = Color.parseColor("#D8C9D8");
            else if (weight >= 4) color = Color.parseColor("#F4A394");
            
            GradientDrawable bg = (GradientDrawable) holder.tvWeight.getBackground().mutate();
            bg.setColor(color);
            
            if (weight >= 3) {
                holder.tvWeight.setTextColor(Color.BLACK);
            } else {
                holder.tvWeight.setTextColor(Color.WHITE);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), AddEditChoreActivity.class);
                intent.putExtra("CHORE_ID", c.id);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return chores.size(); }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivIcon;
            TextView tvName, tvWeight;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivIcon = itemView.findViewById(R.id.iv_icon);
                tvName = itemView.findViewById(R.id.tv_name);
                tvWeight = itemView.findViewById(R.id.tv_weight);
            }
        }
    }
}
