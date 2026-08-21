package com.example.fairnessengine;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Roommate.class, Chore.class, AssignmentLog.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
