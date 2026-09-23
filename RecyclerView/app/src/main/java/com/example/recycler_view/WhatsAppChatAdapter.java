package com.example.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WhatsAppChatAdapter extends RecyclerView.Adapter<WhatsAppChatAdapter.ChatViewHolder> {
    private final ArrayList<ChatItemData> chatData;

    public WhatsAppChatAdapter(ArrayList<ChatItemData> data) {
        this.chatData = data;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_whatsapp_chat_cell_design, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItemData data = chatData.get(position);

        holder.txtUsername.setText(data.getName());
        holder.txtTime.setText(data.getTime());
        holder.txtMsg.setText(data.getMsg());
        holder.imgProfile.setImageResource(data.getImg());

        // Assuming status 2 = Read (Blue Double Tick). Otherwise, Sent (Gray Single Tick).
        if (data.getStatus() == 2) {
            holder.imgStatus.setImageResource(R.drawable.double_tick);
        } else {
            holder.imgStatus.setImageResource(R.drawable.tick);
        }
    }

    @Override
    public int getItemCount() {
        return chatData != null ? chatData.size() : 0;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        final TextView txtUsername;
        final TextView txtMsg;
        final TextView txtTime;
        final ImageView imgProfile;
        final ImageView imgStatus;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }
}