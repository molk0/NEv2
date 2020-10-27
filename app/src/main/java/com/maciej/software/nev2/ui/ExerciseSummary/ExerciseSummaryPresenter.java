package com.maciej.software.nev2.ui.ExerciseSummary;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public class ExerciseSummaryPresenter implements ExerciseSummary.Presenter {

    private ExerciseDao exerciseDao;
    private ExerciseSummary.View viewPresenter;

    public ExerciseSummaryPresenter(ExerciseDao exerciseDao, ExerciseSummary.View viewPresenter) {
        this.exerciseDao = exerciseDao;
        this.viewPresenter = viewPresenter;
    }

    @Override
    public void loadExercises() {
        List<Exercise> exercises = exerciseDao.getAllExercises();
        viewPresenter.showExercises(exercises);
    }

    @Override
    public void loadBottomSheet(@NonNull final Exercise exercise) {
        viewPresenter.showBottomSheet(exercise);
    }

    @Override
    public void loadExerciseHistory(long exerciseId) {
        viewPresenter.showExerciseHistory(exerciseId);
    }

    @Override
    public void loadEditExercise(@NonNull Exercise exercise) {
        viewPresenter.showEditExercise(exercise);
    }

    @Override
    public void removeExercise(@NonNull Exercise exercise) {
        exerciseDao.remove(exercise);
        viewPresenter.afterRemoved();
    }
}
