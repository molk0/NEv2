package com.maciej.software.nev2.ui.DetailHistory;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.DetailDao;
import com.maciej.software.nev2.model.Detail;

import java.util.List;

public class DetailHistoryPresenter implements  DetailHistory.Presenter {

    private DetailHistory.View viewPresenter;
    private DetailDao detailDao;

    public DetailHistoryPresenter(DetailHistory.View viewPresenter, DetailDao detailDao) {
        this.viewPresenter = viewPresenter;
        this.detailDao = detailDao;
    }

    @Override
    public void loadDetails(final long exerciseId) {
        List<Detail> details = detailDao.findDetailsForExercise(exerciseId);
        if (details.size() > 0) {
            viewPresenter.showDetails(details);
        }
    }

    @Override
    public void loadEditDetail(final long detailId) {
        viewPresenter.showEditDetail(detailId);
    }

    @Override
    public void removeDetail(@NonNull Detail detail) {
        detailDao.delete(detail);
        viewPresenter.removeDetail();
    }
}
