package com.maciej.software.nev2.ui.ExerciseSummary;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.RowExerciseSummaryBinding;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.DetailHistory.DetailHistoryFragment;
import com.maciej.software.nev2.ui.EditExercise.EditExerciseActivity;
import com.maciej.software.nev2.ui.ExerciseSummary.BottomSheet.SummaryBottomSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExerciseSummaryFragment extends Fragment implements ExerciseSummary.View,
        SearchView.OnQueryTextListener, SummaryBottomSheet.OnClickListener {

    private static final String EXERCISE_KEY = "exercise";
    private static final int EDIT_REQUEST = 1;

    private ExerciseSummary.Presenter presenter;
    private ExerciseSummaryAdapter adapter;


    public static ExerciseSummaryFragment newInstance() {
        return new ExerciseSummaryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ExerciseSummaryPresenter(
                NEDatabase.getInstance(getContext()).getExerciseDao(), this);
        adapter = new ExerciseSummaryAdapter(new ArrayList<>(0), presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_exercise_summary, container, false);
        setHasOptionsMenu(true);
        setUpRecyclerView(contentView);
        return contentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.loadExercises();
    }

    private void setUpRecyclerView(View view) {
        RecyclerView recycler = view.findViewById(R.id.exercise_summary_recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.summary_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void showExercises(@NonNull final List<Exercise> exercises) {
        adapter.replaceData(exercises);
    }

    @Override
    public void showBottomSheet(@NonNull Exercise exercise) {
        SummaryBottomSheet sheet = SummaryBottomSheet.newInstance(exercise);
        sheet.setTargetFragment(this, 0);
        sheet.show(getFragmentManager(), "BottomSheet");
    }

    @Override
    public void showExerciseHistory(long exerciseId) {
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.summary_list_frame, DetailHistoryFragment.newInstance(exerciseId));
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void showEditExercise(@NonNull Exercise exercise) {
        Intent editExerciseIntent = new Intent(getContext(), EditExerciseActivity.class);
        editExerciseIntent.putExtra(EXERCISE_KEY, exercise);
        startActivityForResult(editExerciseIntent, EDIT_REQUEST);
    }

    @Override
    public void afterRemoved() {
        presenter.loadExercises();
    }

    @Override
    public void onShowHistory(@NonNull Exercise exercise) {
        presenter.loadExerciseHistory(exercise.getId());
    }

    @Override
    public void onEditExercise(@NonNull Exercise exercise) {
        presenter.loadEditExercise(exercise);
    }

    @Override
    public void onRemoveExercise(@NonNull Exercise exercise) {
        presenter.removeExercise(exercise);
    }

    public class ExerciseSummaryAdapter
            extends RecyclerView.Adapter<ExerciseSummaryAdapter.ExerciseSummaryViewHolder>
            implements Filterable {

        private ExerciseSummary.Presenter presenter;
        private List<Exercise> exercises;
        private List<Exercise> exercisesOriginal;

        public ExerciseSummaryAdapter(List<Exercise> exercises, ExerciseSummary.Presenter presenter) {
            setList(exercises);
            this.presenter = presenter;
        }

        public void replaceData(@NonNull List<Exercise> exercises) {
            sortData(exercises);
            setList(exercises);
            notifyDataSetChanged();
        }

        private void setList(@NonNull List<Exercise> exercises) {
            this.exercises = exercises;
            exercisesOriginal = exercises;
        }

        private void sortData(List<Exercise> exercises) {
            Collections.sort(exercises, (ex1, ex2) ->
                    ex1.getName().compareToIgnoreCase(ex2.getName()));
        }

        @Override
        public ExerciseSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RowExerciseSummaryBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.row_exercise_summary, parent, false);
            return new ExerciseSummaryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ExerciseSummaryViewHolder holder, int position) {
            Exercise exercise = exercises.get(position);
            holder.bind(exercise);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                List<Exercise> filteredResults = new ArrayList<>();

                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    //List<Exercise> filteredResults;
                    if (charSequence.length() == 0) {
                        exercises = exercisesOriginal;
                    } else {
                        filteredResults = getFilteredResults(charSequence.toString().toLowerCase());
                        exercises = filteredResults;
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredResults;
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    //exercises = (List<Exercise>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        protected List<Exercise> getFilteredResults(String charSequence) {
            List<Exercise> results = new ArrayList<>();
            for (Exercise e : exercisesOriginal) {
                if (e.getName().toLowerCase().contains(charSequence)) {
                    results.add(e);
                }
            }
            return results;
        }

        public class ExerciseSummaryViewHolder extends RecyclerView.ViewHolder {

            private RowExerciseSummaryBinding binding;

            public ExerciseSummaryViewHolder(RowExerciseSummaryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(@NonNull final Exercise exercise) {
                binding.setExercise(exercise);
                binding.setPresenter(presenter);
                binding.executePendingBindings();
            }
        }
    }

}
