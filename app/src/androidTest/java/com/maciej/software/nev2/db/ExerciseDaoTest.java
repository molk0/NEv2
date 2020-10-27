package com.maciej.software.nev2.db;


import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.model.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class ExerciseDaoTest {
    private ExerciseDao exerciseDao;
    private NEDatabase db;

    @Before
    public void initDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        exerciseDao = db.getExerciseDao();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void getExerciseWhenNoneAdded() throws InterruptedException {
        assertEquals(0, exerciseDao.getAllExercises().size());
    }

    @Test
    public void insertNullValues() {
        Exercise testExercise = new Exercise(null, "5x20", 25);
        exerciseDao.insert(testExercise);
        Exercise newExercise = exerciseDao.getAllExercises().get(0);
        assertEquals(null, newExercise.getName());
    }


    @Test
    public void getAllExercises() {
        final Exercise testExercise = new Exercise("Example Exercise",
                "3x12", 20);
        exerciseDao.insert(testExercise);
        List<Exercise> exercises = exerciseDao.getAllExercises();
        assertNotEquals(0, exercises.size());
    }

    @Test
    public void updateExercise() {
        Exercise testExercise = new Exercise("Example Exercise",
                "3x12", 20);
        exerciseDao.insert(testExercise);

        testExercise = exerciseDao.getAllExercises().get(0);
        testExercise.setWeight(25);
        exerciseDao.update(testExercise);

        Exercise queryExercise = exerciseDao.getAllExercises().get(0);
        assertEquals(25, queryExercise.getWeight());
    }

    @Test
    public void delete() {
        exerciseDao.insert(new Exercise("Example Exercise",
                "3x12", 20));
        List<Exercise> exercises = exerciseDao.getAllExercises();
        exerciseDao.remove(exercises.get(0));
        assertEquals(0, exerciseDao.getAllExercises().size());
    }
}

