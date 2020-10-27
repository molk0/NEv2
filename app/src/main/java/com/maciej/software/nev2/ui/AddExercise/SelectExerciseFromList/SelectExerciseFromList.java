package com.maciej.software.nev2.ui.AddExercise.SelectExerciseFromList;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public interface SelectExerciseFromList {

    interface View {

        void selectExerciseAndGoBack(@NonNull final Exercise exercise);
    }

    interface Presenter {

        void selectExercise(@NonNull final Exercise exercise);

        List<Exercise> loadAllExercises();
    }
}
