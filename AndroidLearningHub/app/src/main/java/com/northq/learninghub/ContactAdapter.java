package com.northq.learninghub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private final Context context;
    private final List<Contact> contacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact c = contacts.get(position);
        holder.name.setText(c.name);
        holder.info.setText(c.number);
        
        if (c.name != null && !c.name.isEmpty()) {
            String[] parts = c.name.split(" ");
            String initials = "";
            if (parts.length > 0) initials += parts[0].substring(0, 1).toUpperCase();
            if (parts.length > 1) initials += parts[1].substring(0, 1).toUpperCase();
            holder.avatar.setText(initials.isEmpty() ? "?" : initials);
        } else {
            holder.avatar.setText("?");
        }

        holder.itemView.setOnClickListener(v -> 
                Toast.makeText(context, "Contact: " + c.name, Toast.LENGTH_SHORT).show());
        
        holder.callAction.setOnClickListener(v -> 
                Toast.makeText(context, "Calling " + c.name + "...", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView avatar, name, info;
        View callAction;
        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.contactAvatar);
            name = itemView.findViewById(R.id.contactName);
            info = itemView.findViewById(R.id.contactInfo);
            callAction = itemView.findViewById(R.id.callAction);
        }
    }
}
