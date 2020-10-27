package com.maciej.software.nev2.ui.DetailHistory.EditDialog;

import android.support.annotation.Nullable;

import com.maciej.software.nev2.model.Detail;

public interface EditDialog {

    interface Presenter {
        Detail loadDetail(final long detailId);
        void saveDetail(Detail detail, String reps, @Nullable String note);
    }
}
