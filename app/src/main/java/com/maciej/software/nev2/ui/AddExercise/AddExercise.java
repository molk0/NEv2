package com.maciej.software.nev2.ui.AddExercise;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public interface AddExercise {

    interface View {

        void showEmptyInputError();

        void showMyExerciseList();

        void showReceivedExercise(Exercise exercise);

        void showWarningDialog(final long exerciseId, final long workoutId);

        void showWorkout();

        void showWorkoutWithError();

        void reset();
    }

    interface Presenter {

        List<String> loadExerciseNames();

        void loadMyExerciseList();

        void loadReceivedExerciseToInputs(@NonNull final Exercise exercise);

        void addNewExercise(@NonNull final Exercise exercise, final long workoutId);

        void addExistingExercise(@NonNull final Exercise exercise, final long workoutId);

        void addExercise(@NonNull final Exercise newExercise, final long workoutId);

        boolean addExerciseToList(final long exerciseId, final long workoutId);

        void addExerciseToListAndFinish(final long exerciseId, final long workoutId);

        void reset();
    }

}
