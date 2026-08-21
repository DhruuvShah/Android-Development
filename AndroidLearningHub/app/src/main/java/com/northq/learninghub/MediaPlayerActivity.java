package com.northq.learninghub;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MediaPlayerActivity extends AppCompatActivity {

    private MusicService musicService;
    private boolean isBound = false;
    private SeekBar seekBar;
    private ImageButton playPauseBtn;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            updateUIState();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (isBound && musicService != null && musicService.isPlaying()) {
                seekBar.setProgress(musicService.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        seekBar = findViewById(R.id.trackSeekBar);
        playPauseBtn = findViewById(R.id.playPauseBtn);

        playPauseBtn.setOnClickListener(v -> togglePlayback());
        
        findViewById(R.id.skipPrevBtn).setOnClickListener(v -> 
                Toast.makeText(this, "Beginning of track", Toast.LENGTH_SHORT).show());
        findViewById(R.id.skipNextBtn).setOnClickListener(v -> 
                Toast.makeText(this, "End of playlist", Toast.LENGTH_SHORT).show());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isBound) musicService.seekTo(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void togglePlayback() {
        if (!isBound) return;
        if (musicService.isPlaying()) {
            musicService.pausePlayback();
        } else {
            musicService.startPlayback();
            handler.post(progressUpdater);
        }
        updateUIState();
    }

    private void updateUIState() {
        if (!isBound) return;
        if (musicService.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            handler.post(progressUpdater);
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
        seekBar.setMax(musicService.getDuration());
        seekBar.setProgress(musicService.getCurrentPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        handler.removeCallbacks(progressUpdater);
    }
}
