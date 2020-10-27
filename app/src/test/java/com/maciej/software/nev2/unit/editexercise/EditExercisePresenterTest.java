package com.maciej.software.nev2.unit.editexercise;

import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.EditExercise.EditExercise;
import com.maciej.software.nev2.ui.EditExercise.EditExercisePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class EditExercisePresenterTest {

    @Mock
    private ExerciseDao daoMock;

    @Mock
    private EditExercise.View viewPresenterMock;

    private EditExercise.Presenter presenter;
    private Exercise testExercise;

    @Before
    public void setUpPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new EditExercisePresenter(daoMock, viewPresenterMock);
        testExercise = new Exercise("test", "test1", 10);
    }

    @Test
    public void editEmptyFields_AndShowError(){
        presenter.editExercise(testExercise,
                "test1", "", "20");
        verify(viewPresenterMock).showEmptyInputError();
    }

    @Test
    public void editExercise_AndShowTheList() {
        presenter.editExercise(testExercise,
                "test1", "test3", "20");

        verify(daoMock).update(any(Exercise.class));
        verify(viewPresenterMock).showExerciseList();
    }

    @Test
    public void editWeightTo0_AndShowTheList() {
        presenter.editExercise(testExercise,
                "test1", "test3", "0");
        verify(daoMock).update(any(Exercise.class));
        verify(viewPresenterMock).showExerciseList();
    }
}
