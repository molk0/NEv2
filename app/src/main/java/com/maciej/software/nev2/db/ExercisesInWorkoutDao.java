package com.maciej.software.nev2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkout;
import com.maciej.software.nev2.model.Workout;

import java.util.List;

@Dao
public interface ExercisesInWorkoutDao {

    @Insert
    void insert(ExercisesInWorkout exerciseInWorkout);

    @Delete
    void removeExercise(final Exercise exercise);

    @Delete
    void removeExerciseInWorkout(final ExercisesInWorkout exercisesInWorkout);

    @Query("SELECT * FROM exercisesinworkout WHERE exerciseId=:exerciseId AND workoutId=:workoutId")
    ExercisesInWorkout getExercisesInWorkoutObject(final long exerciseId, final long workoutId);


    @Query("DELETE FROM exercisesInWorkout WHERE exerciseId=:exerciseId AND workoutId=:workoutId")
    void removeExerciseInWorkout(final long exerciseId, final long workoutId);

    @Query("SELECT * FROM exercise INNER JOIN ExercisesInWorkout " +
            "ON exercise.id=ExercisesInWorkout.exerciseId WHERE " +
            "ExercisesInWorkout.workoutId=:workoutId")
    List<Exercise> getExercisesInWorkout(final long workoutId);

    @Query("SELECT name FROM exercise INNER JOIN ExercisesInWorkout " +
            "ON exercise.id=ExercisesInWorkout.exerciseId WHERE " +
            "ExercisesInWorkout.workoutId=:workoutId")
    List<String> getExerciseNamesInWorkout(final long workoutId);

    @Query("SELECT * FROM workout INNER JOIN ExercisesInWorkout " +
            "ON workout.id=ExercisesInWorkout.workoutId WHERE " +
            "ExercisesInWorkout.exerciseId=:exerciseId LIMIT 1")
    Workout checkIfExerciseInAnyWorkouts(final long exerciseId);
    // ->>> Test
}
