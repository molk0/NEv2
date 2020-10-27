package com.maciej.software.nev2.ui.ExerciseListFragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.FragmentExerciseListBinding;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.EditExercise.EditExerciseActivity;
import com.maciej.software.nev2.ui.ExerciseListFragment.BottomSheetMenu.MainListBottomSheet;
import com.maciej.software.nev2.ui.ExerciseDetail.ExerciseDetailActivity;
import com.maciej.software.nev2.ui.ExerciseListFragment.RemoveExercise.RemoveExerciseDialog;
import com.maciej.software.nev2.ui.AddExercise.AddExerciseActivity;
import com.maciej.software.nev2.ui.ExerciseListFragment.UpdateWeight.UpdateWeightDialog;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListFragment extends Fragment implements ExerciseList.View,
        MainListBottomSheet.OnClickListener, UpdateWeightDialog.OnClickListener,
        RemoveExerciseDialog.OnClickListener, SimpleItemTouchHelperCallback.ItemTouchHelperListener {

    public static final String VERSION_KEY = "version";
    public static final String TYPE_KEY = "type";
    public static final String WORKOUT_KEY = "workoutId";
    public static final String EXERCISE_KEY = "exercise";
    public static final int ADD_DUPLICATE_ERROR_CODE = 2;

    private static final int EDIT_EXERCISE_REQUEST = 2;
    private static final int NEW_EXERCISE_REQUEST = 1;

    private FragmentExerciseListBinding binding;
    private ExerciseAdapter adapter;


    private ExerciseList.Presenter presenter;
    private long workoutId;

    public static ExerciseListFragment newInstance(String dayVersion, String workoutType) {
        Bundle extras = new Bundle();
        extras.putString(VERSION_KEY, dayVersion);
        extras.putString(TYPE_KEY, workoutType);
        ExerciseListFragment frag = new ExerciseListFragment();
        frag.setArguments(extras);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO provide injection for DAOs
        NEDatabase db = NEDatabase
                .getInstance(getContext());
        presenter = new ExerciseListPresenter(db.getExerciseDao(),
                db.getExercisesInWorkoutDao(), db.getWorkoutDao(), this);
        adapter = new ExerciseAdapter(new ArrayList<>(0), presenter);

        if (getArguments() != null) {
            String version = getArguments().getString(VERSION_KEY);
            String workoutType = getArguments().getString(TYPE_KEY);
            workoutId = presenter.findWorkoutId(version, workoutType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_exercise_list,
                container, false);
        binding = FragmentExerciseListBinding.bind(contentView);
        setUpRecyclerView();

        binding.fabAddExercise.setOnClickListener(v ->
                presenter.addNewExercise()
        );

        return contentView;
    }

    private void setUpRecyclerView() {
        RecyclerView recycler = binding.exerciseListView;
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO: Test if this even does anything
        //TODO: Add decorator
        recycler.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration horizontalDivider = new DividerItemDecoration(recycler.getContext(),
                DividerItemDecoration.VERTICAL);
        //recycler.addItemDecoration(horizontalDivider);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadExercises(workoutId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EXERCISE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //TODO snackbar
            } else if (resultCode == ADD_DUPLICATE_ERROR_CODE) {
                makeSnack(R.string.error_duplicate);
            }
        }

        if (requestCode == EDIT_EXERCISE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //TODO  snackbar
            }
        }
    }

    @Override
    public void showExercises(List<Exercise> exercises) {
        adapter.replaceData(exercises);
    }

    @Override
    public void showEmptyListError(boolean show) {
        TextView error = getErrorTextView();
        if (show) {
            if (error.getVisibility() == View.GONE) {
                adapter.clearData();
                error.setVisibility(View.VISIBLE);
            }
        } else {
            if(error.getVisibility()==View.VISIBLE) {
                error.setVisibility(View.GONE);
            }
        }
    }
    private TextView getErrorTextView() {
        return binding.listErrorMessage;
    }

    @Override
    public void showExerciseHistory(Exercise clickedExercise) {
        Intent intent = new Intent(getContext(), ExerciseDetailActivity.class);
        intent.putExtra(EXERCISE_KEY, clickedExercise);
        startActivity(intent);
    }

    @Override
    public void showAddNewExercise() {
        Intent newExerciseIntent = new Intent(getContext(), AddExerciseActivity.class);
        newExerciseIntent.putExtra(WORKOUT_KEY, workoutId);
        startActivityForResult(newExerciseIntent, NEW_EXERCISE_REQUEST);
    }

    @Override
    public void showBottomSheetMenu(Exercise clickedExercise) {
        MainListBottomSheet menu = MainListBottomSheet.newInstance(clickedExercise);
        menu.setTargetFragment(this, 0);
        menu.show(getFragmentManager(), "MenuBottomSheet");
    }

    @Override
    public void showUpdateWeightDialog(Exercise updatedExercise) {
        UpdateWeightDialog updateDialog = UpdateWeightDialog.newInstance(updatedExercise);
        updateDialog.setTargetFragment(this, 1);
        updateDialog.show(getFragmentManager(), "UpdateWeightDialog");
    }

    @Override
    public void showEditExercise(Exercise clickedExercise) {
        Intent editExerciseIntent = new Intent(getContext(), EditExerciseActivity.class);
        editExerciseIntent.putExtra(EXERCISE_KEY, clickedExercise);
        startActivityForResult(editExerciseIntent, EDIT_EXERCISE_REQUEST);
    }

    @Override
    public void showRemoveExercise(Exercise clickedExercise) {
        RemoveExerciseDialog removeDialog = RemoveExerciseDialog.newInstance(clickedExercise);
        removeDialog.setTargetFragment(this, 1);
        removeDialog.show(getFragmentManager(), "RemoveWeightDialog");
    }

    @Override
    public void afterExerciseRemoved() {
        //TODO add an undo button and move it above BNAV
        makeSnack(R.string.exercise_removed);
        refreshList();
    }

    @Override
    public void refreshList() {
        presenter.loadExercises(workoutId);
    }

    private void makeSnack(int stringId) {
        View frame = getActivity().findViewById(R.id.activity_frame);
        Snackbar.make(frame, stringId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
        Exercise exercise = adapter.getItem(holder.getAdapterPosition());
        int targetPos = target.getAdapterPosition();

        presenter.changeExercisePosition(exercise, targetPos);
        adapter.onItemMove(holder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void bottomSheetUpdateWeight(Exercise clickedExercise) {
        presenter.loadUpdateWeightDialog(clickedExercise);
    }

    @Override
    public void bottomSheetEditExercise(Exercise clickedExercise) {
        presenter.loadEditExercise(clickedExercise);
    }

    @Override
    public void bottomSheetRemoveExercise(Exercise clickedExercise) {
        presenter.loadRemoveExercise(clickedExercise);
    }

    @Override
    public void updateWeightDialogClickListener(Exercise updatedExercise, double newWeight) {
        presenter.updateWeightForExercise(updatedExercise, newWeight);
    }

    @Override
    public void removeExercise(@NonNull Exercise exercise, boolean removeFromDb) {
        presenter.removeExercise(exercise, workoutId, removeFromDb);
    }
}
