package com.northq.learninghub;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionRationaleActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_ICON = "extra_icon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_rationale);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String desc = getIntent().getStringExtra(EXTRA_DESC);
        int icon = getIntent().getIntExtra(EXTRA_ICON, R.drawable.ic_camera);

        if (title != null) ((TextView) findViewById(R.id.rationaleTitle)).setText(title);
        if (desc != null) ((TextView) findViewById(R.id.rationaleDescription)).setText(desc);
        ((ImageView) findViewById(R.id.permissionIcon)).setImageResource(icon);

        findViewById(R.id.allowButton).setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        findViewById(R.id.notNowButton).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
