package com.maciej.software.nev2.ui.EditExercise;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.ActivityEditExerciseBinding;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.ExerciseListFragment.ExerciseListFragment;

public class EditExerciseActivity extends AppCompatActivity implements EditExercise.View {

    ActivityEditExerciseBinding binding;
    Exercise exercise;
    EditExercisePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_exercise);
        presenter = new EditExercisePresenter(NEDatabase
                .getInstance(this).getExerciseDao(), this);

        initToolbar();
        setTextInputFieldsToExerciseInfo();

        binding.editExerciseFab.setOnClickListener(view -> passValuesToPresenter());
    }

    private void initToolbar() {
        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_edit_exercise);
        }
    }

    private void setTextInputFieldsToExerciseInfo() {
        exercise = getExercise();
        TextInputEditText nameInput = binding.editName;
        nameInput.setText(exercise.getName());
        binding.editName.setSelection(exercise.getName().length());
        binding.editRepRange.setText(exercise.getRepRange());
        binding.editWeight.setText(exercise.getFormattedWeight());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private Exercise getExercise() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(ExerciseListFragment.EXERCISE_KEY);
    }

    private void passValuesToPresenter() {
        String name = binding.editName.getText().toString();
        String range = binding.editRepRange.getText().toString();
        String weight = binding.editWeight.getText().toString();
        presenter.editExercise(exercise, name, range, weight);
    }

    @Override
    public void showEmptyInputError() {
        TextInputEditText name = binding.editName;
        TextInputEditText range = binding.editRepRange;
        if (name.getText().toString().equals("")) showEmptyNameError();
        if (range.getText().toString().equals("")) showEmptyRangeError();
    }

    private void showEmptyNameError() {
        binding.editLayoutName.setError(getString(R.string.error_empty_name));
    }

    private void showEmptyRangeError() {
        binding.editLayoutRepRange.setError(getString(R.string.error_empty_rep_range));
    }

    @Override
    public void showExerciseList() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
