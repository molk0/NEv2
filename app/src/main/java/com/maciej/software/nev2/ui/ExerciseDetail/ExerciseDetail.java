package com.maciej.software.nev2.ui.ExerciseDetail;

import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Detail;

public interface ExerciseDetail {

    interface View {

        void showRecentDetail(@NonNull final Detail detail);

        void showEmptyDetailError(boolean show);

        void enableMoreDetailsButton(boolean enable);

        void showMoreDetails(final long exerciseId);

        void showNoteInputField(boolean show);

        void showEditCurrentDetail(@NonNull final Detail detail);

        void showSavedDetail();

        void showUpdatedDetail();

        void showRemovedDetail();
    }

    interface Presenter {

        void loadRecentDetail(final long exerciseId);

        void enableMoreDetailsButton(final long exerciseId);

        void loadMoreDetails(final long exerciseId);

        void loadNoteInput();

        void saveDetail(final long detailId, String reps, String note, final long exerciseId);

        void loadEditCurrentDetail(@NonNull final Detail detail);

        void removeDetail(@NonNull final Detail detail);
    }
}
