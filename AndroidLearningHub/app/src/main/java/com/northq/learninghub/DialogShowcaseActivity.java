package com.northq.learninghub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Unit 1 Practical: "Implement custom dialogs and AlertDialog".
 * Covers four common dialog patterns you'll reuse elsewhere in the app
 * (the Expense Tracker's "add expense" and "confirm delete" dialogs use
 * the same custom-view and destructive patterns shown here).
 */
public class DialogShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_showcase);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.simpleDialogBtn).setOnClickListener(v -> showSimpleDialog());
        findViewById(R.id.listDialogBtn).setOnClickListener(v -> showListDialog());
        findViewById(R.id.customDialogBtn).setOnClickListener(v -> showCustomDialog());
        findViewById(R.id.destructiveDialogBtn).setOnClickListener(v -> showDestructiveDialog());
    }

    private void showSimpleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Simple AlertDialog")
                .setMessage("This is the built-in AlertDialog with a title, message, and two buttons.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showListDialog() {
        String[] options = {"XML basics", "UI components", "RecyclerView", "ConstraintLayout"};
        new AlertDialog.Builder(this)
                .setTitle("Pick a Unit 2 topic")
                .setItems(options, (dialog, which) ->
                        Toast.makeText(this, "You picked: " + options[which], Toast.LENGTH_SHORT).show())
                .show();
    }

    private void showCustomDialog() {
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null);
        new AlertDialog.Builder(this)
                .setView(customView)
                .setPositiveButton("Nice", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showDestructiveDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete all local data?")
                .setMessage("This mirrors the confirmation dialog used before deleting an expense in the Expense Tracker.")
                .setPositiveButton("Delete", (dialog, which) ->
                        Toast.makeText(this, "(Demo only — nothing was deleted)", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Keep", null)
                .show();
    }
}
