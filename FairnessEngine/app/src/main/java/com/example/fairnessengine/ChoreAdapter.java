package com.example.fairnessengine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChoreAdapter extends RecyclerView.Adapter<ChoreAdapter.ViewHolder> {
    
    private List<Chore> chores = new ArrayList<>();
    private final OnChoreClickListener listener;
    
    public interface OnChoreClickListener {
        void onChoreClick(Chore chore);
    }
    
    public ChoreAdapter(OnChoreClickListener listener) {
        this.listener = listener;
    }
    
    public void setChores(List<Chore> chores) {
        this.chores = chores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chore c = chores.get(position);
        holder.txtChoreName.setText(c.name);
        holder.txtChoreWeight.setText(String.format("%.1f", c.effortWeight));
        
        int iconRes = c.iconName != null ? ChoreIconUtil.getIconResId(c.iconName) : ChoreIconUtil.guessIconResId(c.name);
        holder.ivIcon.setImageResource(iconRes);
        
        UIUtils.addPressScaleAnimation(holder.itemView);
        holder.itemView.setOnClickListener(v -> listener.onChoreClick(c));
    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtChoreName;
        TextView txtChoreWeight;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChoreName = itemView.findViewById(R.id.txt_chore_name);
            txtChoreWeight = itemView.findViewById(R.id.txt_chore_weight);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
