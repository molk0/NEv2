package com.maciej.software.nev2.unit.exerciselist;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.db.ExercisesInWorkoutDeprecatedDao;
import com.maciej.software.nev2.db.WorkoutDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.Workout;
import com.maciej.software.nev2.ui.ExerciseListFragment.ExerciseList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExerciseListPresenterTest {

    @Mock
    private ExerciseList.View viewPresenter;

    @Mock
    private ExerciseDao exDao;

    @Mock
    private WorkoutDao workDao;

    @Mock
    private ExercisesInWorkoutDeprecatedDao joinDao;

    private static String VERSION = "A";
    private static String TYPE = "TEST";
    private static List<Exercise> exercises;
    private static List<Exercise> emptyList;
    private ExerciseList.Presenter listPresenter;
    private Exercise tempExercise;
    private Workout tempWorkout;

    @Before
    public void setUpPresenter() {
        MockitoAnnotations.initMocks(this);

        //listPresenter = new ExerciseListPresenter(exDao, joinDao, workDao, viewPresenter);
    }

    @Before
    public void initArrays() {
        exercises = new ArrayList<>();
        exercises.add(new Exercise("Name1", "Range1", 10, 1));
        exercises.add(new Exercise("Name2", "Range2", 10, 1));
        exercises.add(new Exercise("Name3", "Range3", 10, 2));

        emptyList = new ArrayList<>(0);
    }

    @Test
    public void getNoWorkoutId_CreateANewTable() {
        long id = 0;
        when(workDao.getWorkoutId("test", "test") ).thenReturn(id);

        listPresenter.findWorkoutId("test", "test");
        verify(workDao).getWorkoutId("test", "test");
        verify(workDao).insertWorkout(any(Workout.class));
    }

    @Test
    public void loadExercisesFromDB_AndLoadIntoView() {
        long workId = 1;
        when(joinDao.findExercisesByWorkoutId(workId)).thenReturn(exercises);

        listPresenter.loadExercises(workId);
        verify(joinDao).findExercisesByWorkoutId(workId);
        verify(viewPresenter).showEmptyListError(false);
        verify(viewPresenter).showExercises(exercises);
    }

    @Test
    public void loadNoExercisesFromDb_AndShowError() {
        long workId = 1;
        when(joinDao.findExercisesByWorkoutId(workId)).thenReturn(emptyList);

        listPresenter.loadExercises(workId);
        verify(joinDao).findExercisesByWorkoutId(workId);
        verify(viewPresenter).showEmptyListError(true);
    }

/*    @Test
    public void loadExercisesFromDBAndLoadIntoView() {
        when(exDao.getItemsByVersionAndWorkoutType(VERSION,TYPE)).thenReturn(exercises);

        listPresenter.loadExercises(VERSION, TYPE);
        verify(exDao).getItemsByVersionAndWorkoutType(VERSION, TYPE);

        verify(viewPresenter).showEmptyListError(false);
        verify(viewPresenter).showExercises(exercises);
    }*/

 /*   @Test
    public void loadNoExercisesFromDBAndShowError() {
        when(exDao.getItemsByVersionAndWorkoutType(VERSION,TYPE)).thenReturn(emptyList);

        listPresenter.loadExercises(VERSION, TYPE);
        verify(exDao).getItemsByVersionAndWorkoutType(VERSION, TYPE);
        verify(viewPresenter).showEmptyListError(true);
    }*/

    @Test
    public void clickFab_openNewExercise() {
        listPresenter.addNewExercise();
        verify(viewPresenter).showAddNewExercise();
    }

    @Test
    public void clickItemButton_loadsBottomSheet() {
        tempExercise = exercises.get(0);
        listPresenter.loadBottomSheetMenu(tempExercise);
        verify(viewPresenter).showBottomSheetMenu(tempExercise);
    }

    @Test
    public void inBottomSheet_clickUpdateWeight() {
        tempExercise = exercises.get(0);
        listPresenter.loadUpdateWeightDialog(tempExercise);
        verify(viewPresenter).showUpdateWeightDialog(tempExercise);
    }

    @Test
    public void updateWeightForExercise_AndRefreshInTheList(){
        tempExercise = exercises.get(0);
        listPresenter.updateWeightForExercise(tempExercise, 50);
        verify(exDao).update(tempExercise);
        verify(viewPresenter).refreshList();
    }

    @Test
    public void inBottomSheet_clickEditExercise() {
        tempExercise = exercises.get(0);
        listPresenter.loadEditExercise(tempExercise);
        verify(viewPresenter).showEditExercise(tempExercise);
    }
    @Test
    public void inBottomSheet_clickRemoveExercise() {
        tempExercise = exercises.get(0);
        //listPresenter.removeExercise(tempExercise);
        verify(exDao).remove(tempExercise);
        verify(viewPresenter).afterExerciseRemoved();
    }





}
