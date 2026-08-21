package com.northq.learninghub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Unit 2 (RecyclerView) + Unit 3 (explicit Intents / navigating between Activities).
 * Every card here launches another Activity with an explicit Intent — this is the
 * primary navigation mechanism of the whole app.
 */
public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder> {

    private final Context context;
    private final List<HubItem> items;

    public HubAdapter(Context context, List<HubItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_hub_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HubItem item = items.get(position);
        holder.icon.setImageResource(item.iconResId);
        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);
        holder.itemView.setOnClickListener(v -> {
            // Explicit intent: we name the target Activity's class directly.
            Intent intent = new Intent(context, item.targetActivity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, subtitle;
        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.cardIcon);
            title = itemView.findViewById(R.id.cardTitle);
            subtitle = itemView.findViewById(R.id.cardSubtitle);
        }
    }
}
