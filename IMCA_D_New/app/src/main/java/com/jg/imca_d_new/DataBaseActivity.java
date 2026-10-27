package com.jg.imca_d_new;

import android.annotation.SuppressLint;
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

public class DataBaseActivity extends AppCompatActivity {

    EditText edtEmail,edtName;
    Button btnInsert,btnView;

    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_base);

        edtEmail=findViewById(R.id.edtEmail);
        edtName=findViewById(R.id.edtName);
        btnInsert=findViewById(R.id.btnInsert);
        btnView=findViewById(R.id.btnView);

        db = openOrCreateDatabase("COMPANY",MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS emp(id INTEGER PRIMARY KEY AUTOINCREMENT,"+"name Text,"+"email TEXT)");

        btnInsert.setOnClickListener(view -> {
            if(edtName.getText().toString().isEmpty()){
                Toast.makeText(DataBaseActivity.this, "please enter name", Toast.LENGTH_SHORT).show();
            }
           if(edtEmail.getText().toString().isEmpty()){
               Toast.makeText(DataBaseActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
           }else{

                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();

               db.execSQL("INSERT INTO emp(name,email) VALUES(?,?)",new String[]{name,email});

                Toast.makeText(DataBaseActivity.this, "Record Inserted...!", Toast.LENGTH_SHORT).show();

                edtName.setText("");
                edtEmail.setText("");

            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataBaseActivity.this, ViewDataActivity.class));
            }
        });

    }
}