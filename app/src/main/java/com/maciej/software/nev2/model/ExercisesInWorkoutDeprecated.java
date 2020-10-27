/*

2nd way

 */

package com.maciej.software.nev2.model;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ExercisesInWorkoutDeprecated {
    @Embedded public Workout workout;

    @Relation(parentColumn = "id",
            entityColumn = "workoutId")
    public List<Exercise> exerciseList;
}