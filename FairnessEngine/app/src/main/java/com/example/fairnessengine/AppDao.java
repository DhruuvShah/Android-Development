package com.example.fairnessengine;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insertRoommate(Roommate roommate);

    @Query("SELECT * FROM roommates WHERE isActive = 1 ORDER BY cumulativeEffort DESC")
    LiveData<List<Roommate>> getAllRoommates();

    @Query("SELECT * FROM roommates WHERE isActive = 1")
    List<Roommate> getAllRoommatesSync();

    @Update
    void updateRoommate(Roommate roommate);

    @Query("SELECT * FROM roommates WHERE id = :id")
    Roommate getRoommateSync(int id);

    @Insert
    long insertChore(Chore chore);

    @Update
    void updateChore(Chore chore);

    @Delete
    void deleteChore(Chore chore);

    @Query("SELECT * FROM chores")
    LiveData<List<Chore>> getAllChores();

    @Query("SELECT * FROM chores")
    List<Chore> getAllChoresSync();

    @Query("SELECT * FROM chores WHERE id = :id")
    Chore getChoreSync(int id);

@Insert
    long insertAssignmentLog(AssignmentLog log);
    
    @Update
    void updateAssignmentLog(AssignmentLog log);
    
    @Query("SELECT * FROM assignment_log WHERE roommateId = :roomId AND completed = 0 AND skipped = 0 ORDER BY dateAssigned DESC")
    LiveData<List<AssignmentLog>> getPendingLogsForRoommate(int roomId);

    @Query("SELECT * FROM assignment_log WHERE completed = 0 AND skipped = 0")
    List<AssignmentLog> getAllPendingLogsSync();

    @Query("SELECT * FROM assignment_log WHERE completed = 1 OR skipped = 1 ORDER BY dateAssigned DESC")
    LiveData<List<AssignmentLog>> getAllAssignmentLogs();

    @Query("SELECT * FROM assignment_log WHERE completed = 1 OR skipped = 1")
    List<AssignmentLog> getAllAssignmentLogsSync();
}
