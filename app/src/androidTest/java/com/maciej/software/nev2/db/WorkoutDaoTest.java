package com.maciej.software.nev2.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.model.Workout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WorkoutDaoTest {

    private WorkoutDao dao;
    private NEDatabase db;

    @Before
    public void initDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        dao = db.getWorkoutDao();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void addWorkout_AndRetrieveIt() {

    }

    @Test
    public void tryToFindWorkout_ThatDoesntExist() {
        long id = dao.getWorkoutId("", "");
        assertTrue(1 > id);
    }

    @Test
    public void findWorkoutThatExists() {
        long id = dao.insertWorkout(new Workout("test", "test2"));
        assertTrue(0 < id);
    }

}
