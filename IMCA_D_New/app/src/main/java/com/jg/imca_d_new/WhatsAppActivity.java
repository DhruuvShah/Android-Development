package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WhatsAppActivity extends AppCompatActivity {

    RecyclerView rvChatList;

    WhatsAppChatAdapter whatsAppChatAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_whats_app);

        rvChatList = findViewById(R.id.rvChatList);

        ArrayList<ChatItemData> datalist = new ArrayList<>();

        datalist.add(new ChatItemData(1,R.drawable.outline_frame_person_24,"Mahi","01:00 PM","Good Afternoon"));
        datalist.add(new ChatItemData(2,R.drawable.imgbg,"Mohit","02:00 PM","assignment nahi kiya hai mene"));
        datalist.add(new ChatItemData(0,R.drawable.img_demo,"Dhurandhar","07:27 PM","part 3 is coming..!"));
        datalist.add(new ChatItemData(1,R.drawable.outline_frame_person_24,"Mahi","01:00 PM","Good Afternoon"));
        datalist.add(new ChatItemData(2,R.drawable.imgbg,"Mohit","02:00 PM","assignment nahi kiya hai mene"));
        datalist.add(new ChatItemData(2,R.drawable.img_demo,"Dhurandhar","07:27 PM","part 3 is coming..!"));



        whatsAppChatAdapter = new WhatsAppChatAdapter(datalist);
        rvChatList.setLayoutManager(new LinearLayoutManager(this));
        rvChatList.setAdapter(whatsAppChatAdapter);

    }
}