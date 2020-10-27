package com.maciej.software.nev2.db;


import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkout;
import com.maciej.software.nev2.model.Workout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class NewJoinTest {

    private ExercisesInWorkoutDao joinDao;
    private WorkoutDao workoutDao;
    private ExerciseDao exerciseDao;
    private NEDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        joinDao = db.getExercisesInWorkoutDao();
        exerciseDao = db.getExerciseDao();
        workoutDao = db.getWorkoutDao();
    }

    @After
    public void closeDb() { db.close(); }

    @Test
    public void addExercisesToWorkout() {
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long workId = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        joinDao.insert(new ExercisesInWorkout(exId, workId));
        long exId2 = exerciseDao.insert(new Exercise("Test5", "Test6", 22.5));
        joinDao.insert(new ExercisesInWorkout(exId2, workId));
        long exId3 = exerciseDao.insert(new Exercise("Test7", "Test8", 27.5));
        long workId2 = workoutDao.insertWorkout(new Workout("Test9", "Test10"));
        joinDao.insert(new ExercisesInWorkout(exId3, workId2));
        joinDao.insert(new ExercisesInWorkout(exId3, workId));
        List<Exercise> myList = joinDao.getExercisesInWorkout(workId);
        List<Exercise> myList2 = joinDao.getExercisesInWorkout(workId2);
        assertEquals(3, myList.size());
        assertEquals(1, myList2.size());
    }

    @Test
    public void deleteExerciseAfterAddingToWorkout() {
        // Populate database
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long exId2 = exerciseDao.insert(new Exercise("Test5", "Test6", 22.5));
        long workId = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        joinDao.insert(new ExercisesInWorkout(exId, workId));
        joinDao.insert(new ExercisesInWorkout(exId2, workId));

        // Delete a given exercise
        Exercise toDelete = exerciseDao.getExercise(exId);
        exerciseDao.remove(toDelete);

        // Make sure the exercise doesn't show in the workout anymore
        List<Exercise> myList = joinDao.getExercisesInWorkout(workId);
        assertEquals(1, myList.size());
    }

    @Test
    public void deleteExerciseOnlyFromWorkout() {
        // Populate
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long workId = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        joinDao.insert(new ExercisesInWorkout(exId, workId));
        List<Exercise> exercises = joinDao.getExercisesInWorkout(workId);
        assertEquals(1, exercises.size());

        // Delete given exercise from the JOIN table
        joinDao.removeExerciseInWorkout(exId, workId);
        exercises = joinDao.getExercisesInWorkout(workId);
        assertEquals(0, exercises.size());
    }

    @Test
    public void deleteWorkoutAndPreserveExercise() {
        // Populate database
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long exId2 = exerciseDao.insert(new Exercise("Test5", "Test6", 22.5));
        long workId = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        joinDao.insert(new ExercisesInWorkout(exId, workId));
        joinDao.insert(new ExercisesInWorkout(exId2, workId));

        // Remove workout
        Workout toDelete = workoutDao.getWorkout(workId);
        workoutDao.delete(toDelete);

        // Make sure the exercises still exist
        List<Exercise> exercises = exerciseDao.getAllExercises();
        assertEquals(2, exercises.size());
    }

    @Test
    public void retrieveListOfExerciseNames() {
        // Populate database
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long exId2 = exerciseDao.insert(new Exercise("Test5", "Test6", 22.5));
        long workId = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        joinDao.insert(new ExercisesInWorkout(exId, workId));
        joinDao.insert(new ExercisesInWorkout(exId2, workId));

        List<String> exerciseNames = joinDao.getExerciseNamesInWorkout(workId);
        assertEquals(2, exerciseNames.size());
        assertEquals("Test", exerciseNames.get(1));
    }

    @Test
    public void checkThatExerciseIsInAnotherWorkout() {
        // Populate database
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long work1Id = workoutDao.insertWorkout(new Workout("Test4", "Test3"));
        long work2Id = workoutDao.insertWorkout(new Workout("Test5", "Test6"));
        joinDao.insert(new ExercisesInWorkout(exId, work1Id));
        joinDao.insert(new ExercisesInWorkout(exId, work2Id));

        Workout workout = joinDao.checkIfExerciseInAnyWorkouts(exId);
        assertNotNull(workout);
    }

    @Test
    public void checkThatExerciseIsNotInOtherWorkouts() {
        long exId = exerciseDao.insert(new Exercise("Test", "Test2", 15.5));
        long work1Id = workoutDao.insertWorkout(new Workout("Test4", "Test3"));

        Workout workout = joinDao.checkIfExerciseInAnyWorkouts(exId);
        assertNull(workout);
    }
}
