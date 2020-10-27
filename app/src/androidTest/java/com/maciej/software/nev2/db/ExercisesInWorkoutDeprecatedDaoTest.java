package com.maciej.software.nev2.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.Workout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExercisesInWorkoutDeprecatedDaoTest {

    private ExercisesInWorkoutDeprecatedDao joinDao;
    private WorkoutDao workoutDao;
    private ExerciseDao exerciseDao;
    private NEDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        exerciseDao = db.getExerciseDao();
        workoutDao = db.getWorkoutDao();
        joinDao = db.getExercisesInWorkoutDao_DEPR();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void getEmptyListWhenNoExercisesInserted() {
        Workout workout = new Workout("Intensity", "A");
        long id = workoutDao.insertWorkout(workout);
        List<Exercise> exercises = joinDao.findExercisesByWorkoutId(id);
        assertEquals(0, exercises.size());
    }

    @Test
    public void insertExerciseInWorkoutAndFindById() {
        Workout workout = new Workout("Intensity", "A");
        Workout workoutB = new Workout("Intensity", "B");
        long firstId = workoutDao.insertWorkout(workout);
        long secondId = workoutDao.insertWorkout(workoutB);
        Exercise exercise = new Exercise("test", "test2", 30, firstId);
        Exercise exerciseB = new Exercise("testB", "testB", 30, secondId);
        exerciseDao.insert(exercise);
        exerciseDao.insert(exerciseB);

        List<Exercise> exercises = joinDao.findExercisesByWorkoutId(secondId);
        assertEquals(1, exercises.size());
        assertEquals("testB", exercises.get(0).getName());
    }

    @Test
    public void insertExerciseWithNoWorkoutId_getEmptyList() {
        Workout workout = new Workout("Intensity", "A");
        long firstId = workoutDao.insertWorkout(workout);
        Exercise exercise = new Exercise("test", "test2", 30);
        exerciseDao.insert(exercise);

        List<Exercise> exercises = joinDao.findExercisesByWorkoutId(firstId);
        assertEquals(0, exercises.size());
    }

    @Test
    public void removeExerciseFromWorkout_ButNotFromDatabase() {
        Workout workout = new Workout("Intensity", "A");
        long firstId = workoutDao.insertWorkout(workout);
        Exercise exercise = new Exercise("test", "test2", 30, firstId);
        long exId = exerciseDao.insert(exercise);
        exercise.setId(exId);
        List<Exercise> exercises = joinDao.findExercisesByWorkoutId(firstId);

        exercise.workoutId = -1;
        exerciseDao.update(exercise);
        exercises = joinDao.findExercisesByWorkoutId(firstId);
        assertEquals(0, exercises.size());
    }

    @Test
    public void removeExerciseFromEntireDatabase() {
        Workout workout = new Workout("Intensity", "A");
        long firstId = workoutDao.insertWorkout(workout);
        Exercise exercise = new Exercise("test", "test2", 30, firstId);
        long exId = exerciseDao.insert(exercise);
        exercise.setId(exId);

        exerciseDao.remove(exercise);
        List<Exercise> exercises = joinDao.findExercisesByWorkoutId(firstId);
        assertEquals(0, exercises.size());
    }

    @Test
    public void insertExerciseInWorkout_ThenMoveToAnotherWorkout() {
        Workout workout = new Workout("Intensity", "A");
        long firstId = workoutDao.insertWorkout(workout);
        Exercise exercise = new Exercise("test", "test2", 30, firstId);
        long exId = exerciseDao.insert(exercise);
        exercise.setId(exId);

        Workout workoutB = new Workout("Intensity", "B");
        long secondId = workoutDao.insertWorkout(workoutB);
        exercise.workoutId = secondId;
        exerciseDao.update(exercise);
        List<Exercise> exercisesB = joinDao.findExercisesByWorkoutId(secondId);
        assertEquals(1, exercisesB.size());
    }
}
