package com.maciej.software.nev2.ui.ExerciseDetail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.FragmentExerciseDetailBinding;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.DetailHistory.DetailHistoryFragment;
import com.maciej.software.nev2.util.TypeFormatter;

import java.util.Date;


public class ExerciseDetailFragment extends Fragment implements ExerciseDetail.View {

    private static final String EXERCISE_KEY = "exercise";

    private FragmentExerciseDetailBinding binding;
    private ExerciseDetail.Presenter presenter;

    private Exercise exercise;
    private Detail currRecentDetail, currLoadedDetail;


    public static ExerciseDetailFragment newInstance(Exercise exercise) {
        Bundle extras = new Bundle();
        extras.putParcelable(EXERCISE_KEY, exercise);
        ExerciseDetailFragment frag = new ExerciseDetailFragment();
        frag.setArguments(extras);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ExerciseDetailPresenter(this, NEDatabase
                .getInstance(getContext()).getDetailDao());

        if (getArguments() != null) {
            exercise = getArguments().getParcelable(EXERCISE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_exercise_detail,
                container, false);
        binding = FragmentExerciseDetailBinding.bind(contentView);

        setRepsEditTextToValue();

        binding.editRecentButton.setOnClickListener(v -> presenter.loadEditCurrentDetail(currRecentDetail));
        binding.addNoteTextButton.setOnClickListener(v -> presenter.loadNoteInput());
        binding.detailSaveButton.setOnClickListener(v -> saveDetail());
        binding.cancelEditButton.setOnClickListener(v -> clearCurrLoaded());
        binding.showMoreButton.setOnClickListener(v -> presenter.loadMoreDetails(exercise.getId()));
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadRecentDetail(exercise.getId());
        presenter.enableMoreDetailsButton(exercise.getId());
    }

    private void setRepsEditTextToValue() {
        TextInputEditText reps = binding.inputRepsDone;
        String weight = getWeightHint();
        reps.setText(weight);
        reps.setSelection(weight.length());
    }

    private String getWeightHint() {
        String weight = exercise.getFormattedWeight();
        weight += "x";
        return weight;
    }

    private void saveDetail() {
        String repRange = binding.inputRepsDone.getText().toString();
        String note = getNoteInputText();
        if (currLoadedDetail == null) {
            presenter.saveDetail(0, repRange, note, exercise.getId());
        } else {
            presenter.saveDetail(currLoadedDetail.getId(), repRange, note, exercise.getId());
            clearCurrLoaded();
        }
        closeKeyboard();
    }

    private void closeKeyboard() {
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    private String getNoteInputText() {
        if (binding.layoutNote.getVisibility() == View.GONE) {
            return "";
        } else {
            return binding.inputNote.getText().toString();
        }
    }

    private void clearCurrLoaded() {
        showCancelEditButton(false);
        clearInputs();
    }

    private void showCancelEditButton(boolean show) {
        if (show) {
            binding.cancelEditButton.setVisibility(View.VISIBLE);
        } else {
            binding.cancelEditButton.setVisibility(View.GONE);
        }
    }

    private void clearInputs() {
        binding.inputRepsDone.setText("");
        binding.inputNote.setText("");
        if (binding.layoutNote.getVisibility() == View.VISIBLE) {
            showNoteInputField(false);
        }
    }

    @Override
    public void showRecentDetail(@NonNull Detail detail) {
        currRecentDetail = detail;
        binding.recentDate.setText(formatDate(detail.getDateAdded()));
        binding.recentReps.setText(detail.getRepsDone());
        binding.recentNote.setText(detail.getNote());
        showEditCurrentButton(true);
    }

    private String formatDate(Date date) {
        return TypeFormatter.formatDate(date);
    }

    @Override
    public void showEmptyDetailError(boolean show) {
        if (show) {
            binding.detailErrorMessage.setVisibility(View.VISIBLE);
        } else {
            binding.detailErrorMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void enableMoreDetailsButton(boolean enable) {
        binding.showMoreButton.setEnabled(enable);
        binding.showMoreButton.setTextColor(getShowMoreButtonColor(enable));
    }

    private int getShowMoreButtonColor(boolean enable) {
        return (enable) ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.appTextInactive);
    }

    @Override
    public void showMoreDetails(final long exerciseId) {
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.detail_content_frame, DetailHistoryFragment.newInstance(exerciseId));
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void showNoteInputField(boolean show) {
        if (show) {
            binding.addNoteTextButton.setVisibility(View.GONE);
            binding.layoutNote.setVisibility(View.VISIBLE);
        } else {
            binding.addNoteTextButton.setVisibility(View.VISIBLE);
            binding.layoutNote.setVisibility(View.GONE);
        }
    }

    /**
     * Sets edit text fields with detail's information for a quick edit
     *
     * @param detail Detail object whose info we use to fill edit text fields
     */
    @Override
    public void showEditCurrentDetail(Detail detail) {
        currLoadedDetail = detail;
        setInputFieldsToDetailInfo(detail);
        showCancelEditButton(true);
        showEditCurrentButton(true);
    }

    private void setInputFieldsToDetailInfo(Detail detail) {
        binding.inputRepsDone.setText(detail.getRepsDone());
        binding.inputRepsDone.setSelection(detail.getRepsDone().length());
        if (!detail.getNote().equals("")) {
            showNoteInputField(true);
            binding.inputNote.setText(detail.getNote());
        } else {
            showNoteInputField(false);
        }
    }

    private void showEditCurrentButton(boolean show) {
        if (show) {
            binding.editRecentButton.setVisibility(View.VISIBLE);
        } else {
            binding.editRecentButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSavedDetail() {
        presenter.loadRecentDetail(exercise.getId());
        Snackbar.make(getActivity().findViewById(R.id.show_more_button), R.string.detail_saved,
                Snackbar.LENGTH_SHORT).show();
        clearInputs();
    }

    @Override
    public void showUpdatedDetail() {
        presenter.loadRecentDetail(exercise.getId());
        Snackbar.make(getActivity().findViewById(R.id.show_more_button), R.string.detail_saved,
                Snackbar.LENGTH_SHORT).show();
        clearInputs();
    }

    @Override
    public void showRemovedDetail() {

    }
}
