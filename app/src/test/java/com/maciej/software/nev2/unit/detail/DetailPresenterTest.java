package com.maciej.software.nev2.unit.detail;

import com.maciej.software.nev2.db.DetailDao;
import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.ui.ExerciseDetail.ExerciseDetail;
import com.maciej.software.nev2.ui.ExerciseDetail.ExerciseDetailPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailPresenterTest {

    @Mock
    private ExerciseDetail.View viewPresenter;

    @Mock
    private DetailDao detailDao;

    private ExerciseDetailPresenter presenter;
    private List<Detail> details;
    private final long exerciseId = 1;

    @Before
    public void setUpPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new ExerciseDetailPresenter(viewPresenter, detailDao);

        details = new ArrayList<>();
        for (int i=0; i<2;i++)
            details.add(new Detail(
                    String.valueOf(i+1), "Note", Calendar.getInstance().getTime(), exerciseId));
    }

    @Test
    public void loadNoRecentDetail_showError() {
        when(detailDao.findRecentDetailForExercise(exerciseId)).thenReturn(null);
        presenter.loadRecentDetail(exerciseId);
        verify(viewPresenter).showEmptyDetailError(true);
    }

    @Test
    public void loadRecentDetail_loadIntoView() {
        when(detailDao.findRecentDetailForExercise(exerciseId)).thenReturn(details.get(1));
        presenter.loadRecentDetail(exerciseId);
        verify(viewPresenter).showEmptyDetailError(false);
        verify(viewPresenter).showRecentDetail(any(Detail.class));
    }

    @Test
    public void getDetailCountOf1_disableMoreDetailsButton() {
        when(detailDao.getDetailCount(exerciseId)).thenReturn(1);
        presenter.enableMoreDetailsButton(exerciseId);
        verify(viewPresenter).enableMoreDetailsButton(false);
    }

    @Test
    public void getDetailList_enableMoreDetailsButton() {
        when(detailDao.getDetailCount(exerciseId)).thenReturn(5);
        presenter.enableMoreDetailsButton(exerciseId);
        verify(viewPresenter).enableMoreDetailsButton(true);
    }

    @Test
    public void saveNewDetailToDb() {
        presenter.saveDetail(0, "Reps", "Note", exerciseId);
        verify(detailDao).insert(any(Detail.class));
        verify(viewPresenter).showSavedDetail();
    }

    @Test
    public void updateDetail() {
        Detail detail = details.get(1);
        detail.setId(99);
        when(detailDao.findDetailWithId(detail.getId())).thenReturn(details.get(1));
        presenter.saveDetail(detail.getId(), "Reps", "Note", exerciseId);
        verify(detailDao).findDetailWithId(detail.getId());
        verify(detailDao).update(details.get(1));
        verify(viewPresenter).showUpdatedDetail();
    }




}
