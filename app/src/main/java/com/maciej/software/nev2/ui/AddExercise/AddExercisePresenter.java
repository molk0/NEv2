package com.maciej.software.nev2.ui.AddExercise;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.db.ExercisesInWorkoutDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkout;
import com.maciej.software.nev2.model.Workout;

import java.util.List;

public class AddExercisePresenter implements AddExercise.Presenter {

    private ExerciseDao exerciseDao;
    private ExercisesInWorkoutDao exercisesInWorkoutDao;
    private AddExercise.View viewPresenter;

    public AddExercisePresenter(ExerciseDao dao, ExercisesInWorkoutDao workoutDao, AddExercise.View view) {
        exerciseDao = dao;
        exercisesInWorkoutDao = workoutDao;
        viewPresenter = view;
    }

    @Override
    public List<String> loadExerciseNames() {
        return exerciseDao.getAllExerciseNames();
    }

    @Override
    public void loadMyExerciseList() {
        viewPresenter.showMyExerciseList();
    }

    @Override
    public void loadReceivedExerciseToInputs(@NonNull final Exercise exercise) {
        viewPresenter.showReceivedExercise(exercise);
    }

    @Override
    public void addNewExercise(@NonNull Exercise exercise, long workoutId) {
        if (inputIsEmpty(exercise)) {
            viewPresenter.showEmptyInputError();
        } else {
            long exerciseId = insertExercise(exercise);
            addExerciseToList(exerciseId, workoutId);
            viewPresenter.showWorkout();
        }
    }

    @Override
    public void addExistingExercise(@NonNull Exercise exercise, long workoutId) {
        if (inputIsEmpty(exercise)) {
            viewPresenter.showEmptyInputError();
        } else {
            checkIfInOtherWorkoutsAndAdd(exercise, workoutId);
        }
    }

    @Override
    public void addExercise(@NonNull final Exercise newExercise, final long workoutId) {
       /* if (inputIsEmpty(newExercise)) {
            viewPresenter.showEmptyInputError();
        } else {
            if (newExercise.getId()!=0) {
                checkIfInOtherWorkoutsAndAdd(newExercise, workoutId);
            } else {
                addNewExerciseToDb(newExercise, workoutId);
                viewPresenter.showWorkout();
            }
        }*/
    }

    private boolean inputIsEmpty(Exercise exercise) {
        return (exercise.getName().equals("") || exercise.getRepRange().equals(""));
    }

    private void addNewExerciseToDb(Exercise newExercise, long workoutId) {
        long exerciseId = insertExercise(newExercise);
        addExerciseToList(exerciseId, workoutId);
    }

    private long insertExercise(@NonNull final Exercise exercise) {
        return exerciseDao.insert(exercise);
    }

    // TODO: Check if workout id is not 0 ?
    @Override
    public boolean addExerciseToList(final long exerciseId, final long workoutId) {
        try {
            exercisesInWorkoutDao.insert(new ExercisesInWorkout(exerciseId, workoutId));
            return true;
        } catch (SQLiteConstraintException e) {
            return false;
        }
    }

    private void checkIfInOtherWorkoutsAndAdd(Exercise exercise, long workoutId) {
        if (checkIfInOtherWorkouts(exercise.getId())) {
            viewPresenter.showWarningDialog(exercise.getId(), workoutId);
        } else {
            addExerciseToListAndFinish(exercise.getId(), workoutId);
        }
    }

    @Override
    public void addExerciseToListAndFinish(long exerciseId, long workoutId) {
        if(addExerciseToList(exerciseId, workoutId)) {
            viewPresenter.showWorkout();
        } else {
            viewPresenter.showWorkoutWithError();
        }

    }
    private boolean checkIfInOtherWorkouts(final long exerciseId) {
        Workout workout = exercisesInWorkoutDao.checkIfExerciseInAnyWorkouts(exerciseId);
        return (workout!=null);
    }


    @Override
    public void reset() {
        viewPresenter.reset();
    }
}
