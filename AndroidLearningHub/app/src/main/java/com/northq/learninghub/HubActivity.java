package com.northq.learninghub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hubRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupGreeting();
        setupRecyclerView();
        setupBottomNav();
    }

    private void setupGreeting() {
        TextView greetingText = findViewById(R.id.greetingText);
        TextView welcomeText = findViewById(R.id.welcomeText);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12) greetingText.setText("Good morning");
        else if (timeOfDay < 16) greetingText.setText("Good afternoon");
        else greetingText.setText("Good evening");

        String email = getIntent().getStringExtra("user_email");
        if (email != null) {
            String name = email.split("@")[0];
            welcomeText.setText("Welcome back, " + name);
        }
    }

    private void setupRecyclerView() {
        List<HubItem> items = new ArrayList<>();
        items.add(new HubItem(R.drawable.ic_lifecycle, "Lifecycle", "Activity Monitoring", LifecycleActivity.class));
        items.add(new HubItem(R.drawable.ic_dialog, "Dialogs", "Custom Showcase", DialogShowcaseActivity.class));
        items.add(new HubItem(R.drawable.ic_camera, "Media", "Camera & Gallery", MediaPickerActivity.class));
        items.add(new HubItem(R.drawable.ic_playground, "Playground", "UI Components", UiPlaygroundActivity.class));
        items.add(new HubItem(R.drawable.ic_help, "Web View", "Online Resources", WebViewActivity.class));
        items.add(new HubItem(R.drawable.ic_quick_actions, "Actions", "Intents Hub", QuickActionsActivity.class));
        items.add(new HubItem(R.drawable.ic_calculator, "Calculator", "Math Utility", CalculatorActivity.class));
        items.add(new HubItem(R.drawable.ic_notes, "Notes", "List View Demo", NotesListActivity.class));
        items.add(new HubItem(R.drawable.ic_expenses, "Hostel", "Management System", ExpenseTrackerActivity.class));
        items.add(new HubItem(R.drawable.ic_settings, "Settings", "Preferences", SettingsActivity.class));
        items.add(new HubItem(R.drawable.ic_music, "Music", "Background Player", MediaPlayerActivity.class));
        items.add(new HubItem(R.drawable.ic_nearby, "Nearby", "Maps & Location", MapsActivity.class));
        items.add(new HubItem(R.drawable.ic_contacts, "Contacts", "Content Provider", ContactsActivity.class));

        RecyclerView recyclerView = findViewById(R.id.hubRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new HubAdapter(this, items));
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        });
        
        findViewById(R.id.notificationButton).setOnClickListener(v -> 
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());
    }
}
