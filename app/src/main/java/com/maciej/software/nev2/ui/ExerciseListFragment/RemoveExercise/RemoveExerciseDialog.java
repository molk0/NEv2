package com.maciej.software.nev2.ui.ExerciseListFragment.RemoveExercise;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.model.Exercise;

public class RemoveExerciseDialog extends DialogFragment {

    private static final String REMOVED_EXERCISE_KEY = "removeExercise";
    private Exercise exercise;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void removeExercise(@NonNull final Exercise exercise, boolean removeFromDb);
    }

    public static RemoveExerciseDialog newInstance(Exercise exercise) {
        RemoveExerciseDialog dialog = new RemoveExerciseDialog();

        Bundle extras = new Bundle();
        extras.putParcelable(REMOVED_EXERCISE_KEY, exercise);
        dialog.setArguments(extras);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercise = getArguments().getParcelable(REMOVED_EXERCISE_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickListener = (OnClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement RemoveExerciseDialog.OnClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_remove_exercise, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Remove exercise");
        builder.setPositiveButton(R.string.button_remove, (dialog, i) -> {
            boolean removeFromDb = false;
            CheckBox checkBox = view.findViewById(R.id.remove_from_db_checkbox);
            if (checkBox.isChecked()) {
                removeFromDb = true;
            }
            onClickListener.removeExercise(exercise, removeFromDb);
        });
        builder.setNegativeButton(R.string.button_cancel, (dialog, i) -> dialog.dismiss());
        return builder.create();
    }

}
