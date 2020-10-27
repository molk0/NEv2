package com.maciej.software.nev2.unit.detail;

import com.maciej.software.nev2.db.DetailDao;
import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.ui.DetailHistory.DetailHistory;
import com.maciej.software.nev2.ui.DetailHistory.DetailHistoryPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailHistoryPresenterTest {

    @Mock
    private DetailHistory.View view;

    @Mock
    private DetailDao dao;

    private DetailHistory.Presenter presenter;
    private List<Detail> details;
    private final long exerciseId = 1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new DetailHistoryPresenter(view, dao);
        details = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            details.add(new Detail(
                    String.valueOf(i + 1), "Note", Calendar.getInstance().getTime(), exerciseId));
    }

    @Test
    public void loadEmptyList_showError() {
        when(dao.findDetailsForExercise(exerciseId)).thenReturn(new ArrayList<>(0));
        presenter.loadDetails(exerciseId);
        //verify(view).showEmptyListError(true);
    }

    @Test
    public void loadDetailList_andShow() {
        when(dao.findDetailsForExercise(exerciseId)).thenReturn(details);
        presenter.loadDetails(exerciseId);
        //verify(view).showEmptyListError(false);
        verify(view).showDetails(details);
    }

    @Test
    public void removeDetail() {
        Detail detail = details.get(0);
        presenter.removeDetail(detail);
        verify(dao).delete(detail);
        verify(view).removeDetail();
    }
}
