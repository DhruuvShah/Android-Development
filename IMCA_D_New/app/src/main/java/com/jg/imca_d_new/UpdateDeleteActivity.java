package com.jg.imca_d_new;

import android.database.Cursor;
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

public class UpdateDeleteActivity extends AppCompatActivity {


    EditText edtID,edtEmail,edtName;
    Button btnSearch,btnUpdate,btnDelete;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_delete);

        edtID=findViewById(R.id.edtID);
        edtEmail=findViewById(R.id.edtEmail);
        edtName=findViewById(R.id.edtName);
        btnSearch=findViewById(R.id.btnSearch);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);


        db = openOrCreateDatabase("COMPANY",MODE_PRIVATE,null);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtID.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }else{

                    Cursor cursor = db.rawQuery("SELECT * FROM emp WHERE id=?",new String[]{edtID.getText().toString()});

                    if(cursor.moveToNext())
                    {
                        edtName.setText(cursor.getString(1));
                        edtEmail.setText(cursor.getString(2));
                    }else{
                        Toast.makeText(UpdateDeleteActivity.this, "Record not found...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtID.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }else if(edtName.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else if(edtEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }else{

                    db.execSQL("UPDATE emp SET name=?,email=? WHERE id=?",new String[]{edtName.getText().toString(),edtEmail.getText().toString(),edtID.getText().toString()});

                    Toast.makeText(UpdateDeleteActivity.this, "Record Updated...!", Toast.LENGTH_SHORT).show();

                    edtID.setText("");
                    edtName.setText("");
                    edtEmail.setText("");
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtID.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter ID", Toast.LENGTH_SHORT).show();
                }else if(edtName.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else if(edtEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDeleteActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }else {

                    db.execSQL("DELETE FROM emp WHERE ID=?",new String[]{edtID.getText().toString()});

                    Toast.makeText(UpdateDeleteActivity.this, "Record Deleted...!", Toast.LENGTH_SHORT).show();

                    edtID.setText("");
                    edtName.setText("");
                    edtEmail.setText("");
                }
            }
        });

    }
}