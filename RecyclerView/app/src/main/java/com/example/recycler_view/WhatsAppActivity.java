package com.example.recycler_view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WhatsAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_whats_app);

        RecyclerView rvChatList = findViewById(R.id.rvChatList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<ChatItemData> fatalist = new ArrayList<>();

        // Status Guide: 0 = Sent (Gray Single Tick), 1 = Delivered (Gray Double Tick), 2 = Read (Gold Double Tick)
        fatalist.add(new ChatItemData(2, R.drawable.image_6ad39f, "Jotaro", "11:59 PM", "Yare Yare Daze... You can't repay what you owe with Money."));
        fatalist.add(new ChatItemData(2, R.drawable.image_6ad384, "Dio Brando", "11:58 PM", "Oh? You're approaching me? Muda muda muda!"));
        fatalist.add(new ChatItemData(1, R.drawable.image_6ad081, "Joseph Joestar", "11:50 PM", "OH MY GOD! Your next line is..."));
        fatalist.add(new ChatItemData(0, R.drawable.image_6ad363, "Polnareff", "11:45 PM", "Bravo! Oh, Bravo! My Silver Chariot is ready."));
        fatalist.add(new ChatItemData(2, R.drawable.image_6ad34a, "Avdol", "11:30 PM", "YES, I AM! Hell 2 U!"));
        fatalist.add(new ChatItemData(1, R.drawable.image_6ad345, "Kakyoin", "11:15 PM", "Rero rero rero... No one can just deflect the Emerald Splash!"));

        WhatsAppChatAdapter whatsAppChatAdapter = new WhatsAppChatAdapter(fatalist);
        rvChatList.setLayoutManager(new LinearLayoutManager(this));
        rvChatList.setAdapter(whatsAppChatAdapter);
    }
}