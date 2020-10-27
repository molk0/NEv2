package com.maciej.software.nev2.ui.DetailHistory;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Detail;

import java.util.List;

public interface DetailHistory {

    interface View {

        void showDetails(List<Detail> details);

        void showEditDetail(final long detailId);

        void removeDetail();
    }

    interface Presenter {

        void loadDetails(final long exerciseId);

        void loadEditDetail(final long detailId);

        void removeDetail(@NonNull Detail detail);
    }
}
