package com.maciej.software.nev2.ui.ExerciseDetail;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.db.DetailDao;
import com.maciej.software.nev2.model.Detail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExerciseDetailPresenter implements ExerciseDetail.Presenter {

    private ExerciseDetail.View viewPresenter;
    private DetailDao detailDao;

    public ExerciseDetailPresenter(@NonNull ExerciseDetail.View viewPresenter,
                                   @NonNull DetailDao detailDao) {
        this.viewPresenter = viewPresenter;
        this.detailDao = detailDao;
    }

    @Override
    public void loadRecentDetail(final long exerciseId) {
        //Detail detail = detailDao.findRecentDetailForExercise(exerciseId);
        List<Detail> details = detailDao.findDetailsForExercise(exerciseId);
        if (details.size() == 0) {
            viewPresenter.showEmptyDetailError(true);
            enableMoreDetailsButton(false);
        } else {
            viewPresenter.showEmptyDetailError(false);
            enableMoreDetailsButton(true);
            viewPresenter.showRecentDetail(details.get(details.size()-1));
        }
    }

    @Override
    public void enableMoreDetailsButton(final long exerciseId) {
/*        int count = detailDao.getDetailCount(exerciseId);
        if (count > 1) {
            viewPresenter.enableMoreDetailsButton(true);
        } else {
            viewPresenter.enableMoreDetailsButton(false);
        }*/
    }

    private void enableMoreDetailsButton(boolean enable) {
        if (enable) {
            viewPresenter.enableMoreDetailsButton(true);
        } else {
            viewPresenter.enableMoreDetailsButton(false);
        }
    }

    @Override
    public void loadMoreDetails(final long exerciseId) {
        viewPresenter.showMoreDetails(exerciseId);
    }

    @Override
    public void loadNoteInput() {
        viewPresenter.showNoteInputField(true);
    }

    @Override
    public void saveDetail(final long detailId, @NonNull String reps, String note,
                           final long exerciseId) {
        if (detailId == 0) {
            saveNewDetail(reps, note, exerciseId);
        } else {
            updateDetail(detailId, reps, note);
        }
    }

    private void saveNewDetail(String reps, String note, final long exerciseId) {
        Date currDate = Calendar.getInstance().getTime();
        Detail detail = new Detail(reps, note, currDate, exerciseId);
        detailDao.insert(detail);
        viewPresenter.showSavedDetail();
    }

    private void updateDetail(final long detailId, String reps, String note) {
        Detail detail = detailDao.findDetailWithId(detailId);
        detail.setRepsDone(reps);
        detail.setNote(note);
        detailDao.update(detail);
        viewPresenter.showUpdatedDetail();
    }

    @Override
    public void loadEditCurrentDetail(@NonNull final Detail detail) {
        viewPresenter.showEditCurrentDetail(detail);
    }

    @Override
    public void removeDetail(@NonNull final Detail detail) {
        detailDao.delete(detail);
        viewPresenter.showRemovedDetail();
    }
}
