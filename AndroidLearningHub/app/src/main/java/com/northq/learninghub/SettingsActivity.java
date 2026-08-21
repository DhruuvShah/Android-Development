package com.northq.learninghub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Unit 3 Practical: "Work with Fragments for multi-pane layouts."
 * Hosts SettingsMasterFragment, which itself hosts SettingsDetailFragment
 * inside its own container — a simple master/detail Fragment-in-Fragment
 * split that mirrors how larger apps swap panes on tablets vs. phones.
 * Unit 4 Practical: "Store and retrieve data using SharedPreferences" backs
 * both the dark-mode switch and the currency choice here.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS = "settings_prefs";
    public static final String KEY_DARK_MODE = "dark_mode";
    public static final String KEY_CURRENCY = "currency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.settingsMasterContainer, new SettingsMasterFragment());
            transaction.commit();
        }
    }
}
