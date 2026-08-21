package com.northq.learninghub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseTrackerActivity extends AppCompatActivity {

    private ExpenseDbHelper dbHelper;
    private ExpenseAdapter adapter;
    private List<Expense> dataList;
    private TextView tabFees, tabComplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_tracker);
        
        dbHelper = new ExpenseDbHelper(this);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        
        tabFees = findViewById(R.id.tabFees);
        tabComplaints = findViewById(R.id.tabComplaints);
        
        RecyclerView recyclerView = findViewById(R.id.expenseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = dbHelper.getAllFees();
        adapter = new ExpenseAdapter(this, dataList, "₹", this::confirmDelete);
        recyclerView.setAdapter(adapter);

        setupTabs();
        
        findViewById(R.id.addExpenseFab).setOnClickListener(v -> 
                startActivity(new Intent(this, AddStudentActivity.class)));
    }

    private void setupTabs() {
        tabFees.setOnClickListener(v -> {
            tabFees.setBackgroundResource(R.drawable.bg_chip_selected);
            tabFees.setTextColor(getResources().getColor(R.color.white));
            tabComplaints.setBackgroundResource(0);
            tabComplaints.setTextColor(getResources().getColor(R.color.manifold_ink_900));
            refreshData(true);
        });

        tabComplaints.setOnClickListener(v -> {
            tabComplaints.setBackgroundResource(R.drawable.bg_chip_selected);
            tabComplaints.setTextColor(getResources().getColor(R.color.white));
            tabFees.setBackgroundResource(0);
            tabFees.setTextColor(getResources().getColor(R.color.manifold_ink_900));
            refreshData(false);
        });
    }

    private void refreshData(boolean isFees) {
        if (isFees) {
            dataList.clear();
            dataList.addAll(dbHelper.getAllFees());
        } else {
            dataList.clear();
            // In a real app we'd load complaints, for demo we show a toast
            Toast.makeText(this, "Complaints list selected", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private void confirmDelete(Expense item, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete this record?")
                .setMessage(item.title)
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteFee(item.id);
                    dataList.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
