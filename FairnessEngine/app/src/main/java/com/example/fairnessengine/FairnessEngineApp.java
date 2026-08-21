package com.example.fairnessengine;

import android.app.Application;
import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FairnessEngineApp extends Application {
    private AppDatabase database;
    public final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, AppDatabase.class, "fairness_engine.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
