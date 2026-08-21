package com.example.fairnessengine;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assignment_log")
public class AssignmentLog {
    @PrimaryKey(autoGenerate = true) public int id;
    public int roommateId;
    public String roommateNameSnapshot;
    public String roommateColorSnapshot;
    public int choreId;
    public String choreNameSnapshot;
    public String choreIconSnapshot;
    public long dateAssigned;
    public boolean completed;
    public boolean skipped;
    public boolean wasManualOverride;
}
