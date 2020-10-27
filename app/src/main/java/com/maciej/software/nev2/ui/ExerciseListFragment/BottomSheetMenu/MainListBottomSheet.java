package com.maciej.software.nev2.ui.ExerciseListFragment.BottomSheetMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.BottomSheetMainListBinding;
import com.maciej.software.nev2.model.Exercise;


public class MainListBottomSheet extends BottomSheetDialogFragment {

    private static final String CLICKED_EXERCISE_KEY = "clickedExercise";

    private Exercise mExercise;
    private OnClickListener onClickListener;

    public interface OnClickListener {

        void bottomSheetUpdateWeight(Exercise clickedExercise);

        void bottomSheetEditExercise(Exercise clickedExercise);

        void bottomSheetRemoveExercise(Exercise clickedExercise);
    }


    public static MainListBottomSheet newInstance(@NonNull final Exercise exercise) {
        MainListBottomSheet bottomSheet = new MainListBottomSheet();

        Bundle args = new Bundle();
        args.putParcelable(CLICKED_EXERCISE_KEY, exercise);
        bottomSheet.setArguments(args);
        return bottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExercise = getArguments().getParcelable(CLICKED_EXERCISE_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickListener = (OnClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainListBottomSheet.OnClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_main_list,
                null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheetMainListBinding bottomSheetBinding = BottomSheetMainListBinding.bind(view);
        bottomSheetBinding.bottomSheetUpdate.setOnClickListener(v -> onUpdateWeight());
        bottomSheetBinding.bottomSheetEdit.setOnClickListener(v -> onEditExercise());
        bottomSheetBinding.bottomSheetRemove.setOnClickListener(v -> onRemoveExercise());
    }

    private void onUpdateWeight() {
        onClickListener.bottomSheetUpdateWeight(mExercise);
        dismiss();
    }

    private void onEditExercise() {
        onClickListener.bottomSheetEditExercise(mExercise);
        dismiss();
    }

    private void onRemoveExercise() {
        onClickListener.bottomSheetRemoveExercise(mExercise);
        dismiss();
    }
}
