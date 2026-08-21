package com.example.fairnessengine;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "roommates")
public class Roommate {
    @PrimaryKey(autoGenerate = true) public int id;
    public String name;
    public String colorHex;
    public double cumulativeEffort;
    public boolean isActive = true;
}
