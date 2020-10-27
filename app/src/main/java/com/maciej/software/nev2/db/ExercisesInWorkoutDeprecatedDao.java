package com.maciej.software.nev2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkoutDeprecated;

import java.util.List;

@Dao
public interface ExercisesInWorkoutDeprecatedDao {

    /*
    @Insert
    void insert(ExercisesInWorkoutDeprecated list);

    @Query("SELECT * FROM exercise
            INNER JOIN exercisesInWorkout ON exercise.id=exerciseInWorkout.exerciseId
            WHERE exerciseInWorkout.workoutId=:workoutId")
    List<Exercise> findExercisesInWorkout(final int workoutId);
     */

    @Query("SELECT * from exercise")
    List<ExercisesInWorkoutDeprecated> getExercisesInWorkout();

    @Query("SELECT * FROM exercise WHERE workoutId=:workoutId")
    List<Exercise> findExercisesByWorkoutId(long workoutId);
}
