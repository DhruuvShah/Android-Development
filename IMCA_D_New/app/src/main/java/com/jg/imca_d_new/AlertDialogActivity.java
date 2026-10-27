package com.jg.imca_d_new;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AlertDialogActivity extends AppCompatActivity {

    AppCompatButton btnAlertDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alert_dialog);

        btnAlertDialog = findViewById(R.id.btnDialog);

        btnAlertDialog.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.outline_frame_person_24);
            builder.setTitle("WhatsApp");
            builder.setMessage("Sure do you want to take break ?");

            builder.setPositiveButton("Yes",(DialogInterface,i)->{
                Toast.makeText(this,"not Allowed",Toast.LENGTH_LONG).show();
                DialogInterface.dismiss();
            });

            builder.setNegativeButton("No",(DialogInterface,i)->{
               Toast.makeText(this,"Allowed",Toast.LENGTH_LONG).show();
               DialogInterface.dismiss();
            });

            builder.setNeutralButton("later",(DialogInterface,i)->{
                Toast.makeText(this,"Allowe later",Toast.LENGTH_LONG).show();
                DialogInterface.dismiss();
            });

            builder.show();
        });

    }
}