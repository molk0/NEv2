package com.maciej.software.nev2.ui.AddExercise.SelectExerciseFromList;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.model.Exercise;

import java.util.List;

public class SelectExerciseFromListPresenter implements SelectExerciseFromList.Presenter {

    private ExerciseDao exerciseDao;
    private SelectExerciseFromList.View viewPresenter;

    public SelectExerciseFromListPresenter(@NonNull final ExerciseDao exerciseDao,
                                           @NonNull final SelectExerciseFromList.View viewPresenter) {
        this.exerciseDao = exerciseDao;
        this.viewPresenter = viewPresenter;
    }

    @Override
    public void selectExercise(@NonNull final Exercise exercise) {
        viewPresenter.selectExerciseAndGoBack(exercise);
    }

    @Override
    public List<Exercise> loadAllExercises() {
        return exerciseDao.getAllExercises();
    }
}
