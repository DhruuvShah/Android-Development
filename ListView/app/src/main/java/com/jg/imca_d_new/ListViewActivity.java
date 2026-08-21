package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListViewActivity extends AppCompatActivity {

    ListView Objlistview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_view);

        Objlistview = findViewById(R.id.lvCountry);

        String[] country = {"India","USA","Russia","China","India","USA","Russia","China",
        "India","USA","Russia","China","India","USA","Russia","China",
        "India","USA","Russia","China","India","USA","Russia","China"};

        ArrayAdapter arrayadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,country);

        Objlistview.setAdapter(arrayadapter);

        Objlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String name = country[position];

                Toast.makeText(ListViewActivity.this,name,Toast.LENGTH_LONG).show();

            }
        });
    }
}











