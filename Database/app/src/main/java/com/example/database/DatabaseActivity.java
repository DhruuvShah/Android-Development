package com.example.database;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DatabaseActivity extends AppCompatActivity {

    EditText edtName, edtEmail;
    Button btnInsert, btnView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_database_acitvity);

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

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);

        db = openOrCreateDatabase("mydb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS emp(id INTEGER PRIMARY KEY AUTOINCREMENT, name Text, email Text)");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().isEmpty()) {
                    Toast.makeText(DatabaseActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (edtEmail.getText().toString().isEmpty()) {
                    Toast.makeText(DatabaseActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    String name = edtName.getText().toString();
                    String email = edtEmail.getText().toString();

                    db.execSQL("INSERT INTO emp(name,email) VALUES(?,?)", new String[]{name, email});
                    Toast.makeText(DatabaseActivity.this, "Record Inserted", Toast.LENGTH_SHORT).show();

                    edtName.setText("");
                    edtEmail.setText("");
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseActivity.this, ViewDataActivity.class));
            }
        });
    }
}