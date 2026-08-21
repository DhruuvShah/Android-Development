package com.example.fairnessengine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chores")
public class Chore {
    @PrimaryKey(autoGenerate = true) public int id;
    public String name;
    public double effortWeight;
    public String frequency;
    public String iconName;
    public String eligibleRoommateIds; // Comma-separated IDs
}
