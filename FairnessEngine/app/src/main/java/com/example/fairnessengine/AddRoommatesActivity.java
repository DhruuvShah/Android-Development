package com.example.fairnessengine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRoommatesActivity extends AppCompatActivity {
    private boolean isManageMode;
    private RecyclerView recyclerAdded;
    private AddedRoommateAdapter adapter;
    private TextView tvAddedCount;
    private Button btnDone;
    
    private final String[] colors = {"#FF6B5B", "#FFB648", "#6FCF97", "#56C2E0", "#C77DFF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roommates);
        
        isManageMode = getIntent().getBooleanExtra("manage_mode", false);
        
        ImageView btnBack = findViewById(R.id.btn_back);
        btnDone = findViewById(R.id.btn_done);
        
        if (isManageMode) {
            btnDone.setText("Done");
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(v -> finish());
            btnDone.setEnabled(true);
            btnDone.setAlpha(1.0f);
        } else {
            btnDone.setText("Let's go");
            btnBack.setVisibility(View.GONE);
            btnDone.setEnabled(false);
            btnDone.setAlpha(0.5f);
        }

        tvAddedCount = findViewById(R.id.tv_added_count);
        
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isManageMode) {
                    finish();
                } else {
                    if (btnDone.isEnabled()) {
                        btnDone.performClick();
                    } else {
                        Toast.makeText(AddRoommatesActivity.this, "Please add at least 2 roommates to continue", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recyclerAdded = findViewById(R.id.recycler_added);
        recyclerAdded.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new AddedRoommateAdapter();
        recyclerAdded.setAdapter(adapter);

        EditText editName = findViewById(R.id.edit_roommate_name);
        ImageView btnAdd = findViewById(R.id.btn_add_roommate);

        btnAdd.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            if (!name.isEmpty()) {
                Roommate r = new Roommate();
                r.name = name;
                r.cumulativeEffort = 0;
                r.isActive = true;
                
                FairnessEngineApp app = (FairnessEngineApp) getApplication();
                app.executorService.execute(() -> {
                    AppDao dao = app.getDatabase().appDao();
                    List<Roommate> existing = dao.getAllRoommatesSync();
                    int idx = existing.size();
                    r.colorHex = colors[idx % colors.length];
                    dao.insertRoommate(r);
                });
                editName.setText("");
            }
        });

        btnDone.setOnClickListener(v -> {
            // Disabled navigation to HomeDashboardActivity
            Toast.makeText(this, "Setup complete! Other features are currently disabled.", Toast.LENGTH_LONG).show();
        });

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }

        AppDatabase db = ((FairnessEngineApp) getApplication()).getDatabase();
        db.appDao().getAllRoommates().observe(this, roommates -> {
            if (roommates != null) {
                tvAddedCount.setText("Added (" + roommates.size() + ")");
                adapter.setRoommates(roommates);
                
                View tvInstruction = findViewById(R.id.tv_instruction);
                if (roommates.size() >= 2) {
                    if (tvInstruction != null) tvInstruction.setVisibility(View.GONE);
                    if (!isManageMode) {
                        btnDone.setEnabled(true);
                        btnDone.setAlpha(1.0f);
                    }
                } else {
                    if (tvInstruction != null) tvInstruction.setVisibility(View.VISIBLE);
                    if (!isManageMode) {
                        btnDone.setEnabled(false);
                        btnDone.setAlpha(0.5f);
                    }
                }
            }
        });
    }

    private static class AddedRoommateAdapter extends RecyclerView.Adapter<AddedRoommateAdapter.ViewHolder> {
        private List<Roommate> roommates = new ArrayList<>();
        
        public void setRoommates(List<Roommate> list) {
            this.roommates = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roommate_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Roommate r = roommates.get(position);
            holder.tvName.setText(r.name);
            if (!r.name.isEmpty()) {
                holder.tvAvatar.setText(r.name.substring(0, 1).toUpperCase());
            }
            int color = Color.parseColor(r.colorHex != null ? r.colorHex : "#FF6B5B");
            GradientDrawable bg = (GradientDrawable) holder.tvAvatar.getBackground().mutate();
            bg.setColor(color);
            
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                    .setTitle("Remove Roommate")
                    .setMessage("Remove " + r.name + "? Their past history stays, but they won't be assigned new chores.")
                    .setPositiveButton("Remove", (dialog, which) -> {
                        FairnessEngineApp app = (FairnessEngineApp) v.getContext().getApplicationContext();
                        app.executorService.execute(() -> {
                            r.isActive = false;
                            app.getDatabase().appDao().updateRoommate(r);
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
            
            holder.itemView.setOnClickListener(v -> {
                EditText input = new EditText(v.getContext());
                input.setText(r.name);
                input.setSelection(r.name.length());
                
                new AlertDialog.Builder(v.getContext())
                    .setTitle("Edit Roommate")
                    .setView(input)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newName = input.getText().toString().trim();
                        if (!newName.isEmpty() && !Objects.equals(newName, r.name)) {
                            FairnessEngineApp app = (FairnessEngineApp) v.getContext().getApplicationContext();
                            app.executorService.execute(() -> {
                                r.name = newName;
                                app.getDatabase().appDao().updateRoommate(r);
                            });
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }

        @Override
        public int getItemCount() { return roommates.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvAvatar, tvName;
            ImageView btnDelete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAvatar = itemView.findViewById(R.id.tv_avatar);
                tvName = itemView.findViewById(R.id.tv_name);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }
        }
    }
}
