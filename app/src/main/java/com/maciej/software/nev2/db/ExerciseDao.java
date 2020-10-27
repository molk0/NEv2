package com.maciej.software.nev2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.maciej.software.nev2.model.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE id = :id")
    Exercise getExercise(long id);

    @Query("SELECT * FROM exercise")
    List<Exercise> getAllExercises();

    @Query("SELECT DISTINCT name FROM exercise")
    List<String> getAllExerciseNames();

    @Query("SELECT COUNT(*) from exercise")
    int countItems();

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void remove(Exercise exercise);

    @Query("DELETE FROM exercise WHERE id=:id")
    void removeById(final long id);
}
