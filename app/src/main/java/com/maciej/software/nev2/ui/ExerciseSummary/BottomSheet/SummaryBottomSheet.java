package com.maciej.software.nev2.ui.ExerciseSummary.BottomSheet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.BottomSheetSummaryBinding;
import com.maciej.software.nev2.model.Exercise;

public class SummaryBottomSheet extends BottomSheetDialogFragment {

    private static final String EXERCISE_KEY = "exercise";
    private Exercise exercise;
    private OnClickListener listener;

    public interface OnClickListener {
        void onShowHistory(@NonNull final Exercise exercise);
        void onEditExercise(@NonNull final Exercise exercise);
        void onRemoveExercise(@NonNull final Exercise exercise);
    }


    public static SummaryBottomSheet newInstance(@NonNull final Exercise exercise) {
        SummaryBottomSheet sheet = new SummaryBottomSheet();
        Bundle extras = new Bundle();
        extras.putParcelable(EXERCISE_KEY, exercise);
        sheet.setArguments(extras);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercise = getArguments().getParcelable(EXERCISE_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            + " must implement SummaryBottomSheet.OnClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_summary,
                null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomSheetSummaryBinding binding = BottomSheetSummaryBinding.bind(view);
        binding.summaryHistory.setOnClickListener(l-> onShowHistory());
        binding.summaryEdit.setOnClickListener(l-> onEditExercise());
        binding.summaryRemove.setOnClickListener(l-> onRemoveExercise());

    }

    private void onShowHistory() {
        listener.onShowHistory(exercise);
        dismiss();
    }

    private void onEditExercise() {
        listener.onEditExercise(exercise);
        dismiss();
    }

    private void onRemoveExercise() {
        listener.onRemoveExercise(exercise);
        dismiss();
    }
}
