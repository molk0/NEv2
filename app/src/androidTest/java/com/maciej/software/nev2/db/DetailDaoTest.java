package com.maciej.software.nev2.db;

import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.model.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.test.MoreAsserts.assertNotEqual;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class DetailDaoTest {

    private DetailDao dao;
    private ExerciseDao exerciseDao;

    private Calendar calendar;
    private NEDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        dao = db.getDetailDao();
        exerciseDao = db.getExerciseDao();
        calendar = Calendar.getInstance();
    }

    private long insertAndGetExerciseId() {
        Exercise testExercise = new Exercise("Test", "Test", 20);
        return exerciseDao.insert(testExercise);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void findNullDetail() {
        Detail detail = dao.findDetailWithId(0);
        assertNull(detail);
    }

    @Test
    public void addEmptyDetail() {
        long exerciseId = insertAndGetExerciseId();
        dao.insert(new Detail("", "", calendar.getTime(), exerciseId));
        List<Detail> details = dao.findDetailsForExercise(exerciseId);
        assertEquals(1, details.size());
    }

    @Test(expected = SQLiteConstraintException.class)
    public void addDetailsForExerciseThatDoesntExist() {
        dao.insert(new Detail("Test", "Test", calendar.getTime(), 2));
    }

    @Test
    public void addDetail_FindItById() {
        long id = insertAndGetExerciseId();
        Detail newDetail = new Detail("40x8", "Note", calendar.getTime(), id);
        long detailId = dao.insertAndReturnId(newDetail);
        Detail dbDetail = dao.findDetailWithId(detailId);
        assertEquals(newDetail.getRepsDone(), dbDetail.getRepsDone());
        assertEquals(newDetail.getNote(), dbDetail.getNote());
        assertEquals(newDetail.getDateAdded(), dbDetail.getDateAdded());
    }

    @Test
    public void getDetailCount() {
        long exerciseId = insertAndGetExerciseId();
        dao.insert(new Detail("8 7 6", "Note2", calendar.getTime(), exerciseId));
        dao.insert(new Detail("8 7 5", "Note2", calendar.getTime(), exerciseId));
        int count = dao.getDetailCount(exerciseId);
        assertEquals(2, count);
    }

    @Test
    public void findDetailsForGivenExercise_WhenNoneAdded() {
        long exerciseId = insertAndGetExerciseId();
        List<Detail> details = dao.findDetailsForExercise(exerciseId);
        assertEquals(0, details.size());
    }

    @Test
    public void findDetailForGivenExercise() {
        long exerciseId = insertAndGetExerciseId();
        dao.insert(new Detail("8 7 6", "Note", calendar.getTime(), exerciseId));
        List<Detail> details = dao.findDetailsForExercise(exerciseId);
        assertEquals(1, details.size());
    }

    @Test
    public void findDetail_andUpdate() {
        long exerciseId = insertAndGetExerciseId();
        dao.insert(new Detail("8 7 6", "Note", calendar.getTime(), exerciseId));
        Detail recent = dao.findDetailWithId(exerciseId);
        recent.setRepsDone("8 8 8");
        dao.update(recent);
        Detail updated = dao.findDetailWithId(exerciseId);
        assertEquals(recent.getRepsDone(), updated.getRepsDone());
    }

    @Test
    public void findMostRecentDetailForExercise() {
        // Create two notes with different dates, make sure they are different
        long exerciseId = insertAndGetExerciseId();
        Date date1 = calendar.getTime();
        Detail detail = new Detail("8 7 6", "Note", date1, exerciseId);

        Date date2 = new Date(date1.getTime() + (1000*60*60));
        Detail detail2 = new Detail("8 7 7", "Note", date2, exerciseId);
        assertNotEqual(date1, date2);

        // Check that the most recent date is pulled from the db
        dao.insert(detail);
        dao.insert(detail2);
        Detail recent = dao.findRecentDetailForExercise(exerciseId);
        assertEquals(detail2.getNote(), recent.getNote());
        assertEquals(detail2.getDateAdded(), recent.getDateAdded());
    }
}
