package com.maciej.software.nev2.ui.ExerciseSummary;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public interface ExerciseSummary {

    interface View {
        void showExercises(@NonNull final List<Exercise> exercises);
        void showBottomSheet(@NonNull final Exercise exercise);
        void showExerciseHistory(final long exerciseId);
        void showEditExercise(@NonNull final Exercise exercise);
        void afterRemoved();
    }

    interface Presenter {

        void loadExercises();
        void loadBottomSheet(@NonNull final Exercise exercise);
        void loadExerciseHistory(final long exerciseId);
        void loadEditExercise(@NonNull final Exercise exercise);
        void removeExercise(@NonNull final Exercise exercise);
    }
}
