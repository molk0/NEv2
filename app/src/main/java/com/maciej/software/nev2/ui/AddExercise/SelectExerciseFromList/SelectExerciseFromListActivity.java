package com.maciej.software.nev2.ui.AddExercise.SelectExerciseFromList;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.RowSelectExerciseBinding;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.AddExercise.AddExerciseActivity;

import java.util.List;

public class SelectExerciseFromListActivity extends AppCompatActivity
        implements SelectExerciseFromList.View {

    private SelectExerciseFromList.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise_from_list);
        presenter = new SelectExerciseFromListPresenter(NEDatabase.getInstance(this).getExerciseDao(),
                this);

        setUpToolbar();
        setUpRecyclerView();
    }

    private void setUpToolbar() {
        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_add_from_list);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recycler = findViewById(R.id.select_exercise_recycler);
        List<Exercise> exercises = getExerciseList();
        SelectExerciseAdapter adapter = new SelectExerciseAdapter(exercises, presenter);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    private List<Exercise> getExerciseList() {
        return presenter.loadAllExercises();
    }

    @Override
    public void selectExerciseAndGoBack(@NonNull Exercise exercise) {
        Intent result = new Intent();
        result.putExtra(AddExerciseActivity.EXERCISE_RETURN_KEY, exercise);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public class SelectExerciseAdapter
            extends RecyclerView.Adapter<SelectExerciseAdapter.ItemViewHolder> {

        private List<Exercise> exercises;
        private SelectExerciseFromList.Presenter presenter;

        public SelectExerciseAdapter(@NonNull final List<Exercise> exercises,
                                     @NonNull final SelectExerciseFromList.Presenter presenter) {
            this.exercises = exercises;
            this.presenter = presenter;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RowSelectExerciseBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.row_select_exercise, parent, false);
            return new ItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Exercise exercise = exercises.get(position);
            holder.bind(exercise);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private RowSelectExerciseBinding binding;

            public ItemViewHolder(RowSelectExerciseBinding binding) {
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
