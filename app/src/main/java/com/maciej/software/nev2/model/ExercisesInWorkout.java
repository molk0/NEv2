package com.maciej.software.nev2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"exerciseId", "workoutId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workoutId",
                        onDelete = CASCADE)})
public class ExercisesInWorkout {

    public long exerciseId;
    public long workoutId;
    public int positionInList;

    public ExercisesInWorkout(long exerciseId, long workoutId) {
        this.exerciseId = exerciseId;
        this.workoutId = workoutId;
    }
}
