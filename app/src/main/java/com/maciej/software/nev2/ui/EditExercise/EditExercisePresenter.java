package com.maciej.software.nev2.ui.EditExercise;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.util.TypeFormatter;

public class EditExercisePresenter implements EditExercise.Presenter {

    private ExerciseDao exerciseDao;
    private EditExercise.View viewPresenter;

    public EditExercisePresenter(ExerciseDao dao, EditExercise.View view) {
        exerciseDao = dao;
        viewPresenter = view;
    }

    @Override
    public void editExercise(@NonNull Exercise exercise, final String name,
                             final String repRange, final String weight) {
        if (name.equals("") || repRange.equals("")) {
            viewPresenter.showEmptyInputError();
        } else {
            exercise = updateValues(exercise, name, repRange, weight);
            exerciseDao.update(exercise);
            viewPresenter.showExerciseList();
        }
    }

    private Exercise updateValues(Exercise exercise, String name, String range, String weight) {
        exercise.setName(name);
        exercise.setRepRange(range);
        exercise.setWeight(TypeFormatter.parseWeight(weight));
        return exercise;
    }
}
