package com.example.fairnessengine;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.activity.OnBackPressedCallback;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;

public class AddEditChoreActivity extends AppCompatActivity {
    private double selectedEffort = 3.0;
    private String selectedFrequency = "WEEKLY";
    private String selectedIconName = "ic_other";
    private Set<Integer> selectedRoommateIds = new HashSet<>();
    
    private EditText editName;
    private int existingChoreId = -1;
    private boolean isDirty = false;
    private boolean isInitializing = true;
    private boolean userEditedName = false;
    private IconAdapter iconAdapter;
    private AssigneeToggleAdapter assigneeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_chore);

        existingChoreId = getIntent().getIntExtra("CHORE_ID", -1);
        editName = findViewById(R.id.edit_chore_name);

        findViewById(R.id.btn_close).setOnClickListener(v -> attemptClose());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { attemptClose(); }
        });

        editName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { 
                if (!isInitializing) {
                    isDirty = true;
                    // If the name is changed and it's not one of the default names, mark as user edited
                    if (!isDefaultName(s.toString().trim())) {
                        userEditedName = true;
                    }
                }
            }
        });

        // Icon Picker
        RecyclerView recyclerIcons = findViewById(R.id.recycler_icons);
        recyclerIcons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        iconAdapter = new IconAdapter(name -> {
            selectedIconName = name;
            if (!isInitializing) {
                isDirty = true;
                String currentName = editName.getText().toString().trim();
                // If the field is empty or contains one of the default names, update it
                if (currentName.isEmpty() || isDefaultName(currentName)) {
                    editName.setText(ChoreIconUtil.getDefaultName(name));
                    userEditedName = false;
                }
            }
        });
        recyclerIcons.setAdapter(iconAdapter);

        // Effort Toggle
        MaterialButtonToggleGroup toggleEffort = findViewById(R.id.toggle_effort);
        toggleEffort.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_effort_low) selectedEffort = 1.0;
                else if (checkedId == R.id.btn_effort_med) selectedEffort = 3.0;
                else if (checkedId == R.id.btn_effort_high) selectedEffort = 5.0;
                if (!isInitializing) isDirty = true;
            }
        });

        // Frequency Toggle
        MaterialButtonToggleGroup toggleFreq = findViewById(R.id.toggle_frequency);
        toggleFreq.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.freq_daily) selectedFrequency = "DAILY";
                else if (checkedId == R.id.freq_weekly) selectedFrequency = "WEEKLY";
                else if (checkedId == R.id.freq_needed) selectedFrequency = "AS_NEEDED";
                if (!isInitializing) isDirty = true;
            }
        });

        // Assignees
        RecyclerView recyclerAssignees = findViewById(R.id.recycler_assignees);
        recyclerAssignees.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        assigneeAdapter = new AssigneeToggleAdapter(id -> {
            if (selectedRoommateIds.contains(id)) selectedRoommateIds.remove(id);
            else selectedRoommateIds.add(id);
            if (!isInitializing) isDirty = true;
        });
        recyclerAssignees.setAdapter(assigneeAdapter);

        loadInitialData();

        View.OnClickListener saveListener = v -> saveChore();
        findViewById(R.id.btn_save_top).setOnClickListener(saveListener);
        findViewById(R.id.btn_save_chore).setOnClickListener(saveListener);
    }

    private void loadInitialData() {
        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            List<Roommate> allRoommates = dao.getAllRoommatesSync();
            
            if (existingChoreId != -1) {
                Chore c = dao.getChoreSync(existingChoreId);
                runOnUiThread(() -> {
                    if (c != null) {
                        editName.setText(c.name);
                        userEditedName = !isDefaultName(c.name); // Set based on existing name
                        selectedIconName = c.iconName != null ? c.iconName : "ic_other";
                        selectedFrequency = c.frequency;
                        selectedEffort = c.effortWeight;
                        
                        if (c.eligibleRoommateIds != null && !c.eligibleRoommateIds.isEmpty()) {
                            for (String id : c.eligibleRoommateIds.split(",")) {
                                try { selectedRoommateIds.add(Integer.parseInt(id.trim())); } catch (Exception ignored) {}
                            }
                        } else {
                            for (Roommate r : allRoommates) selectedRoommateIds.add(r.id);
                        }

                        applyInitialUI(allRoommates);
                    }
                    isInitializing = false;
                });
            } else {
                for (Roommate r : allRoommates) selectedRoommateIds.add(r.id);
                runOnUiThread(() -> {
                    applyInitialUI(allRoommates);
                    isInitializing = false;
                });
            }
        });
    }

    private void applyInitialUI(List<Roommate> allRoommates) {
        iconAdapter.setSelectedIcon(selectedIconName);
        assigneeAdapter.setData(allRoommates, selectedRoommateIds);
        
        MaterialButtonToggleGroup toggleEffort = findViewById(R.id.toggle_effort);
        if (selectedEffort <= 1.5) toggleEffort.check(R.id.btn_effort_low);
        else if (selectedEffort <= 3.5) toggleEffort.check(R.id.btn_effort_med);
        else toggleEffort.check(R.id.btn_effort_high);

        MaterialButtonToggleGroup toggleFreq = findViewById(R.id.toggle_frequency);
        if ("DAILY".equals(selectedFrequency)) toggleFreq.check(R.id.freq_daily);
        else if ("WEEKLY".equals(selectedFrequency)) toggleFreq.check(R.id.freq_weekly);
        else toggleFreq.check(R.id.freq_needed);
    }

    private void saveChore() {
        String name = editName.getText().toString().trim();
        if (name.isEmpty()) {
            editName.setError("Name required");
            return;
        }

        String eligibleIds = selectedRoommateIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        FairnessEngineApp app = (FairnessEngineApp) getApplication();
        app.executorService.execute(() -> {
            AppDao dao = app.getDatabase().appDao();
            Chore c = (existingChoreId != -1) ? dao.getChoreSync(existingChoreId) : new Chore();
            if (c == null) return;
            
            c.name = name;
            c.effortWeight = selectedEffort;
            c.frequency = selectedFrequency;
            c.iconName = selectedIconName;
            c.eligibleRoommateIds = eligibleIds;

            if (existingChoreId != -1) dao.updateChore(c);
            else dao.insertChore(c);
            
            runOnUiThread(this::finish);
        });
    }

    private boolean isDefaultName(String name) {
        if (name == null || name.isEmpty()) return true;
        for (String icon : ChoreIconUtil.getAllIconNames()) {
            if (name.equals(ChoreIconUtil.getDefaultName(icon))) return true;
        }
        return name.equals(ChoreIconUtil.getDefaultName(null));
    }

    private void attemptClose() {
        if (isDirty) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Discard changes?")
                .setMessage("Are you sure you want to discard unsaved changes?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            finish();
        }
    }

    private static class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {
        private final String[] icons = ChoreIconUtil.getAllIconNames();
        private String selectedIcon = "ic_other";
        private final OnIconSelectedListener listener;
        public interface OnIconSelectedListener { void onIconSelected(String name); }
        public IconAdapter(OnIconSelectedListener listener) { this.listener = listener; }
        public void setSelectedIcon(String name) { this.selectedIcon = name; notifyDataSetChanged(); }

        @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_picker, parent, false);
            return new ViewHolder(v);
        }
        @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = icons[position];
            holder.ivIcon.setImageResource(ChoreIconUtil.getIconResId(name));
            boolean isSelected = name.equals(selectedIcon);
            holder.bgSelection.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);
            
            int color = ChoreIconUtil.getIconColor(name);
            holder.ivIcon.setColorFilter(color);
            
            holder.itemView.setOnClickListener(v -> {
                selectedIcon = name;
                notifyDataSetChanged();
                listener.onIconSelected(name);
            });
        }
        @Override public int getItemCount() { return icons.length; }
        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivIcon; View bgSelection;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivIcon = itemView.findViewById(R.id.iv_icon);
                bgSelection = itemView.findViewById(R.id.bg_selection);
            }
        }
    }

    private static class AssigneeToggleAdapter extends RecyclerView.Adapter<AssigneeToggleAdapter.ViewHolder> {
        private List<Roommate> roommates = new ArrayList<>();
        private Set<Integer> selectedIds = new HashSet<>();
        private final OnToggleListener listener;
        public interface OnToggleListener { void onToggle(int id); }
        public AssigneeToggleAdapter(OnToggleListener listener) { this.listener = listener; }
        public void setData(List<Roommate> list, Set<Integer> selected) {
            this.roommates = list;
            this.selectedIds = selected;
            notifyDataSetChanged();
        }
        @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignee_toggle, parent, false);
            return new ViewHolder(v);
        }
        @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Roommate r = roommates.get(position);
            holder.chip.setText(r.name);
            holder.chip.setChecked(selectedIds.contains(r.id));
            holder.chip.setOnClickListener(v -> {
                listener.onToggle(r.id);
            });
        }
        @Override public int getItemCount() { return roommates.size(); }
        static class ViewHolder extends RecyclerView.ViewHolder {
            Chip chip;
            public ViewHolder(@NonNull View itemView) { super(itemView); chip = itemView.findViewById(R.id.chip_assignee); }
        }
    }
}
