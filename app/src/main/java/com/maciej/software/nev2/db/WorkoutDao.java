package com.maciej.software.nev2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.maciej.software.nev2.model.Workout;

@Dao
public interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertWorkout(Workout workout);

    @Query("SELECT id FROM workout WHERE type=:type AND version=:version")
    long getWorkoutId(String type, String version);

    @Query("SELECT count(*) FROM workout WHERE type=:type AND version=:version")
    int findIfTableExists(String type, String version);

    @Query("SELECT * FROM workout WHERE id=:workoutId")
    Workout getWorkout(final long workoutId);

    @Delete
    void delete(Workout workout);
}
