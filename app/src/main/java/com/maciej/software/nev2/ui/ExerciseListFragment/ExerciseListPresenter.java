package com.maciej.software.nev2.ui.ExerciseListFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.db.ExercisesInWorkoutDao;
import com.maciej.software.nev2.db.WorkoutDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.Workout;

import java.util.List;

public class ExerciseListPresenter implements ExerciseList.Presenter {

    private ExerciseDao exerciseDao;
    //private ExercisesInWorkoutDeprecatedDao joinDao;
    private ExercisesInWorkoutDao exercisesInWorkoutDao;
    private WorkoutDao workDao;
    private ExerciseList.View viewPresenter;

    public ExerciseListPresenter(ExerciseDao exerciseDao, ExercisesInWorkoutDao exercisesInWorkoutDao,
                                 WorkoutDao workDao, ExerciseList.View viewPresenter) {
        this.exerciseDao = exerciseDao;
        this.exercisesInWorkoutDao = exercisesInWorkoutDao;
        this.workDao = workDao;
        this.viewPresenter = viewPresenter;
    }

    /**
     *  Tries to find the workout that's currently displayed on the screen.
     *      If it doesn't exist, it creates a new one.
     *
     * @param type Type of workout (ex. Intensity, Volume, Push, Pull...)
     * @param version Version of a workout type (ex. Week 1 of Intensity workout)
     * @return id of the given workout (or a freshly created one if it didn't exist)
     */
    @Override
    public long findWorkoutId(@Nullable final String type, @Nullable final String version) {
        long id = workDao.getWorkoutId(type, version);
        if (id < 1) {
            id = createNewWorkout(type, version);
        }
        return id;
    }

    private long createNewWorkout(final String type, final String version) {
        return workDao.insertWorkout(new Workout(type, version));
    }

    //TODO do it on the background thread
    public void loadExercises(long workoutId) {
        //List<Exercise> exercises = joinDao.findExercisesByWorkoutId(workoutId);
        List<Exercise> exercises = exercisesInWorkoutDao.getExercisesInWorkout(workoutId);
        if (exercises.size() == 0) {
            viewPresenter.showEmptyListError(true);
        } else {
            viewPresenter.showEmptyListError(false);
            viewPresenter.showExercises(exercises);
        }
    }

    @Override
    public void loadExerciseHistory(@NonNull final Exercise clickedExercise) {
        viewPresenter.showExerciseHistory(clickedExercise);
    }

    @Override
    public void changeExercisePosition(@NonNull final Exercise exercise, int end) {
        long id = exercise.getId();
        // do this shit in the background
    }

    @Override
    public void addNewExercise() {
        viewPresenter.showAddNewExercise();
    }

    @Override
    public void loadBottomSheetMenu(@NonNull final Exercise clickedExercise) {
        viewPresenter.showBottomSheetMenu(clickedExercise);
    }

    @Override
    public void loadUpdateWeightDialog(@NonNull final Exercise clickedExercise) {
        viewPresenter.showUpdateWeightDialog(clickedExercise);
    }

    @Override
    public void updateWeightForExercise(@NonNull final Exercise exercise, final double weight) {
        exercise.setWeight(weight);
        exerciseDao.update(exercise);
        viewPresenter.refreshList();
    }

    @Override
    public void loadEditExercise(@NonNull final Exercise clickedExercise) {
        viewPresenter.showEditExercise(clickedExercise);
    }

    @Override
    public void loadRemoveExercise(@NonNull final Exercise clickedExercise) {
        viewPresenter.showRemoveExercise(clickedExercise);
    }

    @Override
    public void removeExercise(@NonNull final Exercise exercise, final long workoutId,
                               boolean removeFromDb) {
        if (removeFromDb) {
            exerciseDao.remove(exercise);

        } else {
            exercisesInWorkoutDao.removeExerciseInWorkout(exercise.getId(), workoutId);
        }
        viewPresenter.afterExerciseRemoved();
    }
}
