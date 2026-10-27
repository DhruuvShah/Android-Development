package com.jg.imca_d_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WhatsAppChatAdapter extends RecyclerView.Adapter<WhatsAppChatAdapter.SubClass>{

    ArrayList<ChatItemData> chatData = new ArrayList<>();

    public WhatsAppChatAdapter(ArrayList<ChatItemData> data){
        this.chatData = data;
    }



    @NonNull
    @Override  // assign the design file // raw design file
    public SubClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_whatsapp_chat_cell_design,parent,false);
        return new SubClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubClass holder, int position) {

        ChatItemData data = chatData.get(position);  // 1 > img, name, msg, time  4 // 2 img , name, msg , time

        holder.txtFname.setText(data.name);
        holder.txtTime.setText(data.time);
        holder.txtmsg.setText(data.msg);
        holder.img.setImageResource(data.img);

        if(data.status == 1)
        {
            holder.imgStatus.setImageResource(R.drawable.double_tick);
        } else if (data.status == 2) {
            holder.imgStatus.setImageResource(R.drawable.blur_tick);
        } else{
            holder.imgStatus.setImageResource(R.drawable.single_tick);
        }


    }

    @Override // for show data
    public int getItemCount() {
        return chatData.size();
    }

    class SubClass extends RecyclerView.ViewHolder{

        TextView txtFname,txtmsg,txtTime;
        ImageView img,imgStatus;
        public SubClass(@NonNull View itemView) {
            super(itemView);

            txtFname = itemView.findViewById(R.id.txtUsername);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtmsg = itemView.findViewById(R.id.txtMsg);

            img = itemView.findViewById(R.id.imgProfile);
            imgStatus = itemView.findViewById(R.id.imgStatus);

        }
    }
}
