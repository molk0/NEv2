package com.maciej.software.nev2.ui.ExerciseListFragment.UpdateWeight;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.model.Exercise;


public class UpdateWeightDialog extends DialogFragment {

    private static final String UPDATED_EXERCISE_KEY = "updatedExercise";
    private Exercise mExercise;
    private double mNewWeight;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void updateWeightDialogClickListener(Exercise updatedExercise, double weight);
    }

    public static UpdateWeightDialog newInstance(Exercise exercise) {
        UpdateWeightDialog dialog = new UpdateWeightDialog();

        Bundle extras = new Bundle();
        extras.putParcelable(UPDATED_EXERCISE_KEY, exercise);
        dialog.setArguments(extras);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExercise = getArguments().getParcelable(UPDATED_EXERCISE_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickListener = (OnClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UpdateWeightDialog.OnClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_update_weight, container,
                false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.button_update, null);
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_update_weight, null));
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(l -> {
                boolean done = false;
                TextInputEditText newWeight = dialog.findViewById(R.id.input_dialog_update_weight);
                String newWeightText = newWeight.getText().toString();
                if (newWeightText.equals("")) {
                    showError(dialog);
                } else {
                    mNewWeight = Double.parseDouble(newWeightText);
                    onClickListener.updateWeightDialogClickListener(mExercise, mNewWeight);
                    done = true;
                }
                if (done) dialog.dismiss();
            });
        }
    }

    private void showError(AlertDialog dialog) {
        TextInputLayout til = dialog.findViewById(R.id.layout_dialog_update_weight);
        til.setError(getString(R.string.error_empty_weight));
    }
}
