package com.example.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            float density = getResources().getDisplayMetrics().density;
            int horizontalPadding = (int) (16 * density);
            int verticalPadding = (int) (12 * density);
            v.setPadding(
                    systemBars.left + horizontalPadding,
                    systemBars.top + verticalPadding,
                    systemBars.right + horizontalPadding,
                    systemBars.bottom + verticalPadding
            );
            return insets;
        });

        txtData = findViewById(R.id.txtData);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        db = openOrCreateDatabase("mydb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS emp(id INTEGER PRIMARY KEY AUTOINCREMENT, name Text, email Text)");

        Cursor cursor = db.rawQuery("SELECT * FROM emp", null);
        String data = "";

        while (cursor.moveToNext()) {
            data += "ID: " + cursor.getInt(0) + "\n";
            data += "NAME: " + cursor.getString(1) + "\n";
            data += "EMAIL: " + cursor.getString(2) + "\n\n";
        }
        cursor.close();

        if (data.isEmpty()) {
            data = "No records found.";
        }

        txtData.setText(data);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewDataActivity.this, UpdateDeleteActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        db = openOrCreateDatabase("mydb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS emp(id INTEGER PRIMARY KEY AUTOINCREMENT, name Text, email Text)");

        Cursor cursor = db.rawQuery("SELECT * FROM emp", null);
        String data = "";

        while (cursor.moveToNext()) {
            data += "ID: " + cursor.getInt(0) + "\n";
            data += "NAME: " + cursor.getString(1) + "\n";
            data += "EMAIL: " + cursor.getString(2) + "\n\n";
        }
        cursor.close();

        if (data.isEmpty()) {
            data = "No records found.";
        }

        txtData.setText(data);
    }
}