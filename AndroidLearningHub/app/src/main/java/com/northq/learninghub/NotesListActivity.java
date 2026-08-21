package com.northq.learninghub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Unit 3 Practical: "Implement a ListView with an ArrayAdapter."
 * Kept deliberately simple (flat text list) — RecyclerView (used in the
 * Expense Tracker) is the more capable sibling covered in the very next
 * practical, so this screen exists specifically to show the older, simpler
 * ListView + ArrayAdapter pattern for comparison.
 */
public class NotesListActivity extends AppCompatActivity {

    private static final String PREFS = "notes_prefs";
    private static final String KEY_NOTES = "notes_set";

    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        notes = new ArrayList<>(new LinkedHashSet<>(prefs.getStringSet(KEY_NOTES, new LinkedHashSet<>())));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        ListView listView = findViewById(R.id.notesListView);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            notes.remove(position);
            adapter.notifyDataSetChanged();
            persist();
            return true;
        });

        EditText input = findViewById(R.id.noteInput);
        findViewById(R.id.addNoteBtn).setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                notes.add(0, text);
                adapter.notifyDataSetChanged();
                input.setText("");
                persist();
            }
        });
    }

    private void persist() {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putStringSet(KEY_NOTES, new LinkedHashSet<>(notes))
                .apply();
    }
}
