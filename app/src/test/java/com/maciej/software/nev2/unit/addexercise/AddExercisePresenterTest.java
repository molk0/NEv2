package com.maciej.software.nev2.unit.addexercise;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.db.ExercisesInWorkoutDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkout;
import com.maciej.software.nev2.model.Workout;
import com.maciej.software.nev2.ui.AddExercise.AddExercise;
import com.maciej.software.nev2.ui.AddExercise.AddExercisePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddExercisePresenterTest {

    @Mock
    private ExerciseDao exerciseDao;

    @Mock
    private ExercisesInWorkoutDao joinDao;

    @Mock
    private AddExercise.View viewPresenter;

    private AddExercise.Presenter presenter;
    private final long workoutId = 1;

    @Before
    public void setUpPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new AddExercisePresenter(exerciseDao, joinDao, viewPresenter);
    }

    @Test
    public void addEmptyExercise_AndShowError() {
        Exercise newExercise = new Exercise("", "", 10);
        presenter.addNewExercise(newExercise, workoutId);
        verify(viewPresenter).showEmptyInputError();
    }


    @Test
    public void addNewExercise_AddToAndShowExerciseList() {
        final Exercise newExercise = new Exercise
                (1, "test1", "test2", 3);
        presenter.addNewExercise(newExercise, workoutId);
        verify(exerciseDao).insert(newExercise);
        verify(joinDao).insert(any(ExercisesInWorkout.class));
        verify(viewPresenter).showWorkout();
    }

    @Test
    public void addExistingExercise_AddToTheList() {
        final Exercise exercise = new Exercise
                (1, "test1", "test2", 3);
        when(joinDao.checkIfExerciseInAnyWorkouts(exercise.getId())).thenReturn(null);
        presenter.addExistingExercise(exercise, workoutId);
        verify(joinDao).checkIfExerciseInAnyWorkouts(exercise.getId());
        verify(joinDao).insert(any(ExercisesInWorkout.class));
        verify(viewPresenter).showWorkout();
    }

    @Test
    public void addExistingExercise_displayDialog() {
        final Exercise exercise = new Exercise
                (1, "test1", "test2", 3);
        Workout workout = new Workout("test", "test");
        when(joinDao.checkIfExerciseInAnyWorkouts(exercise.getId())).thenReturn(workout);
        presenter.addExistingExercise(exercise, workoutId);
        verify(joinDao).checkIfExerciseInAnyWorkouts(exercise.getId());
        verify(viewPresenter).showWarningDialog(exercise.getId(), workoutId);
    }

}