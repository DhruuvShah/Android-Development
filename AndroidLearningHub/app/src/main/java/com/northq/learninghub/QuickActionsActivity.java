package com.northq.learninghub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Unit 3 Practical: "Use explicit and implicit intents for sharing, calling,
 * emailing, and accessing the camera."
 * Implicit intents below declare an ACTION and let Android's package manager
 * resolve which installed app handles it. The final button uses an explicit
 * intent, naming CalculatorActivity directly — the same mechanism the Hub
 * screen uses for all of its navigation cards.
 */
public class QuickActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_actions);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.shareBtn).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Learning Android development with Java via the iMCA syllabus!");
            startActivity(Intent.createChooser(share, "Share via"));
        });

        findViewById(R.id.callBtn).setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890"));
            safelyStart(call);
        });

        findViewById(R.id.emailBtn).setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
            email.putExtra(Intent.EXTRA_SUBJECT, "Hello from Learning Hub");
            safelyStart(email);
        });

        findViewById(R.id.cameraBtn).setOnClickListener(v -> {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            safelyStart(camera);
        });

        findViewById(R.id.explicitBtn).setOnClickListener(v ->
                startActivity(new Intent(this, CalculatorActivity.class)));
    }

    private void safelyStart(Intent intent) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No app found to handle this action", Toast.LENGTH_SHORT).show();
        }
    }
}
