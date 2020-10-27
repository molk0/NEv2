package com.maciej.software.nev2.ui.DetailHistory.EditDialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Detail;

public class DetailHistoryEditDialog extends DialogFragment {

    private static final String DETAIL_KEY = "detailKey";
    private EditDialog.Presenter presenter;
    private Detail detail;
    //private DialogDetailHistoryEditBinding binding;

    public static DetailHistoryEditDialog newInstance(long detailId) {
        DetailHistoryEditDialog dialog = new DetailHistoryEditDialog();
        Bundle extras = new Bundle();
        extras.putLong(DETAIL_KEY, detailId);
        dialog.setArguments(extras);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EditDialogPresenter(NEDatabase.getInstance(getActivity()).getDetailDao());
        if (getArguments() != null) {
            long detailId = getArguments().getLong(DETAIL_KEY);
            detail = presenter.loadDetail(detailId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_detail_history_edit, container, false);
        //binding = DialogDetailHistoryEditBinding.bind(contentView);
        //binding.setDetail(detail);
        return contentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_detail_history_edit, null);
        displayDetailInfo(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.button_update, positiveButtonClick);
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss());
        return builder.create();
    }

    private DialogInterface.OnClickListener positiveButtonClick = (dialog, i) -> {
        AlertDialog editDialog = (AlertDialog) getDialog();
        saveEditedDetail(editDialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
    };

    private void saveEditedDetail(Dialog dialog) {
        TextInputEditText reps = dialog.findViewById(R.id.detail_history_edit_reprange);
        TextInputEditText note = dialog.findViewById(R.id.detail_history_edit_note);
        String newReps = reps.getText().toString();
        String newNote = note.getText().toString();
        presenter.saveDetail(detail, newReps, newNote);
    }

    private void displayDetailInfo(View view) {
        TextView date = view.findViewById(R.id.detail_history_date);
        date.setText(detail.getFormattedDate());
        TextInputEditText reps = view.findViewById(R.id.detail_history_edit_reprange);
        reps.setText(detail.getRepsDone());
        TextInputEditText note = view.findViewById(R.id.detail_history_edit_note);
        note.setText(detail.getNote());
    }
}
