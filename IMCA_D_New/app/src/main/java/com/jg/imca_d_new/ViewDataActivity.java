package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewDataActivity extends AppCompatActivity {

    TextView txtData;
    ImageView btnBack;
    Button btnEdit;

    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_data);

        txtData = findViewById(R.id.txtData);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        db = openOrCreateDatabase("COMPANY",MODE_PRIVATE,null);

        Cursor cursor = db.rawQuery("SELECT * FROM emp",null);

        String data = "";

        while (cursor.moveToNext())
        {
            data += "ID : "+ cursor.getInt(0) + "\n";
            data += "NAME : " + cursor.getString(1) + "\n";
            data += "EMAIL : " + cursor.getString(2) + "\n\n";


        }

        txtData.setText(data);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewDataActivity.this,UpdateDeleteActivity.class));
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        db = openOrCreateDatabase("COMPANY",MODE_PRIVATE,null);

        Cursor cursor = db.rawQuery("SELECT * FROM emp",null);

        String data = "";

        while (cursor.moveToNext())
        {
            data += "ID : "+ cursor.getInt(0) + "\n";
            data += "NAME : " + cursor.getString(1) + "\n";
            data += "EMAIL : " + cursor.getString(2) + "\n\n";


        }

        txtData.setText(data);
    }
}