package com.maciej.software.nev2.ui.ExerciseListFragment;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public interface ExerciseList {

    interface View {

        void showExercises(List<Exercise> exercises);

        void showEmptyListError(boolean show);

        void showExerciseHistory(Exercise clickedExercise);

        void showAddNewExercise();

        void showBottomSheetMenu(Exercise clickedExercise);

        void showUpdateWeightDialog(Exercise clickedExercise);

        void showEditExercise(Exercise clickedExercise);

        void showRemoveExercise(Exercise clickedExercise);

        void afterExerciseRemoved();

        // void afterExerciseUpdated();

        // void showAddedExercise();

        // void showEditedExercise();

        void refreshList();
    }

    interface Presenter {

        long findWorkoutId(final String type, final String version);

        void loadExercises(final long workoutId);

        void loadExerciseHistory (@NonNull final Exercise clickedExercise);

        void changeExercisePosition(@NonNull final Exercise exercise, int end);

        void addNewExercise();

        void loadBottomSheetMenu(@NonNull final Exercise clickedExercise);

        void loadUpdateWeightDialog(@NonNull final Exercise clickedExercise);

        void updateWeightForExercise(@NonNull final Exercise exercise, double weight);

        void loadEditExercise(@NonNull final Exercise clickedExercise);

        void loadRemoveExercise(@NonNull final Exercise clickedExercise);

        void removeExercise(@NonNull final Exercise exercise, final long workoutId,
                            boolean removeFromDb);
    }
}
