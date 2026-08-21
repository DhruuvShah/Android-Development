package com.northq.learninghub;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    public interface OnDeleteListener { void onDelete(Expense expense, int position); }

    private final Context context;
    private final List<Expense> expenses;
    private final String currencySymbol;
    private final OnDeleteListener deleteListener;

    public ExpenseAdapter(Context context, List<Expense> expenses, String currencySymbol, OnDeleteListener deleteListener) {
        this.context = context;
        this.expenses = expenses;
        this.currencySymbol = currencySymbol;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense e = expenses.get(position);
        holder.title.setText(e.title);
        holder.date.setText(e.date);
        holder.amount.setText(String.format(Locale.getDefault(), "%s%,.0f", currencySymbol, e.amount));
        holder.status.setText(e.status);

        if ("Paid".equalsIgnoreCase(e.status)) {
            holder.status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.manifold_primary_container)));
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.manifold_primary));
        } else {
            holder.status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.manifold_ink_300)));
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.manifold_ink_600));
        }

        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) deleteListener.onDelete(expenses.get(pos), pos);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, amount, status;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feeTitle);
            date = itemView.findViewById(R.id.feeDate);
            amount = itemView.findViewById(R.id.feeAmount);
            status = itemView.findViewById(R.id.feeStatus);
        }
    }
}
