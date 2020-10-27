package com.maciej.software.nev2.ui.DetailHistory.EditDialog;

import android.support.annotation.Nullable;

import com.maciej.software.nev2.db.DetailDao;
import com.maciej.software.nev2.model.Detail;

public class EditDialogPresenter implements EditDialog.Presenter {

    private DetailDao detailDao;

    public EditDialogPresenter(DetailDao detailDao) {
        this.detailDao = detailDao;
    }

    @Override
    public Detail loadDetail(long detailId) {
        return detailDao.findDetailWithId(detailId);
    }

    @Override
    public void saveDetail(Detail detail, String reps, @Nullable String note) {
        detail.setRepsDone(reps);
        detail.setNote(note);
        detailDao.update(detail);
    }
}
