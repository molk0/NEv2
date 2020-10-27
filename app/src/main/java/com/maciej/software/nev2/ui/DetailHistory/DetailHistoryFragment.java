package com.maciej.software.nev2.ui.DetailHistory;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.ui.DetailHistory.EditDialog.DetailHistoryEditDialog;

import java.util.ArrayList;
import java.util.List;


public class DetailHistoryFragment extends Fragment implements DetailHistory.View,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String ID_KEY = "exerciseId";
    private static final int EDIT_REQ_CODE = 1;

    private long exerciseId;
    private DetailHistory.Presenter presenter;
    private DetailHistoryAdapter adapter;

    public static DetailHistoryFragment newInstance(long exerciseId) {
        Bundle extras = new Bundle();
        extras.putLong(ID_KEY, exerciseId);
        DetailHistoryFragment frag = new DetailHistoryFragment();
        frag.setArguments(extras);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NEDatabase db = NEDatabase.getInstance(getContext());
        presenter = new DetailHistoryPresenter(this, db.getDetailDao());
        adapter = new DetailHistoryAdapter(new ArrayList<>(0), presenter);

        if (getArguments() != null) {
            exerciseId = getArguments().getLong(ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_detail_history,
                container, false);

        RecyclerView recycler = contentView.findViewById(R.id.detail_history_list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(decoration);
        recycler.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadDetails(exerciseId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ_CODE)
            if (resultCode == Activity.RESULT_OK) {
                refreshList();
            }
    }

    @Override
    public void showDetails(List<Detail> details) {
        adapter.replaceData(details);
    }

    private void refreshList() {
        presenter.loadDetails(exerciseId);
    }

    @Override
    public void showEditDetail(long detailId) {
        DetailHistoryEditDialog dialog = DetailHistoryEditDialog.newInstance(detailId);
        dialog.setTargetFragment(this, EDIT_REQ_CODE);
        dialog.show(getFragmentManager(), "editDetailDialog");
    }

    @Override
    public void removeDetail() {
        int count = adapter.getItemCount();

        // THIS SHOULD BE SEPARATED!! and needs its own function
        if (count == 0 && isAdded() && !isDetached() && !isRemoving()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction, int position) {
        if (holder instanceof DetailHistoryAdapter.DetailViewHolder) {
            final Detail detail = adapter.getItem(position);
            final int detailPosition = holder.getAdapterPosition();
            adapter.removeItem(holder.getAdapterPosition());
            showRemovedSnackbar(detail, detailPosition);
        }
    }

    private void showRemovedSnackbar(final Detail detail, final int position) {
        View frame = getActivity().findViewById(R.id.content_frame);
        Snackbar snackbar = Snackbar.make(frame, R.string.detail_removed, Snackbar.LENGTH_SHORT);

        // Remove temporarily from the adapter
        snackbar.setAction(R.string.button_undo, view ->
                adapter.restoreItem(detail, position));

        // When Undo action not taken, remove from the data source
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                    presenter.removeDetail(detail);
                }
            }
        });
        snackbar.show();
    }
}
