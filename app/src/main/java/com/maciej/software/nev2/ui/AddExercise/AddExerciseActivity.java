package com.maciej.software.nev2.ui.AddExercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.AddExercise.SelectExerciseFromList.SelectExerciseFromListActivity;
import com.maciej.software.nev2.ui.ExerciseListFragment.ExerciseListFragment;
import com.maciej.software.nev2.util.TypeFormatter;

import java.util.List;


public class AddExerciseActivity extends AppCompatActivity implements AddExercise.View {

    public static final String EXERCISE_RETURN_KEY = "addExercise";

    private static final int SELECT_EXERCISE_REQ = 1;

    private AddExercise.Presenter presenter;
    private AutoCompleteTextView name;
    private TextInputEditText repRange, weight;
    private Exercise selectedExercise;
    private long workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        presenter = new AddExercisePresenter(
                NEDatabase.getInstance(this).getExerciseDao(),
                NEDatabase.getInstance(this).getExercisesInWorkoutDao(),
                this);

        setUpToolbar();
        workoutId = getWorkoutId();
        assignEditTexts();
        setAutoCompleteAdapter();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        FloatingActionButton fab = findViewById(R.id.save_exercise_fab);
        fab.setOnClickListener(view -> addNewExercise());
        // Button saveButton = findViewById(R.id.save_exercise_button);
        //saveButton.setOnClickListener(l -> addNewExercise());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_EXERCISE_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                selectedExercise = data.getParcelableExtra(EXERCISE_RETURN_KEY);
                presenter.loadReceivedExerciseToInputs(selectedExercise);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_exercise_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_exercise_list:
                presenter.loadMyExerciseList();
                return true;
             default:
                 return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        ActionBar ab = getSupportActionBar();
        if (ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_add_exercise);
        }
    }

    private long getWorkoutId() {
        Intent intent = getIntent();
        return intent.getLongExtra(ExerciseListFragment.WORKOUT_KEY, 0);
    }

    private void assignEditTexts() {
        name = findViewById(R.id.input_name);
        repRange = findViewById(R.id.input_rep_range);
        weight = findViewById(R.id.input_weight);
    }

    private void setAutoCompleteAdapter() {
        List<String> adapterListOfNames = getExerciseNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item,
                adapterListOfNames);
        name.setAdapter(adapter);
    }

    private List<String> getExerciseNames() {
        return presenter.loadExerciseNames();
    }

    @Override
    public void showReceivedExercise(Exercise exercise) {
        name.setText(exercise.getName());
        repRange.setText(exercise.getRepRange());
        weight.setText(TypeFormatter.getFormattedWeight(exercise.getWeight()));
    }

    private void addNewExercise() {
        if (selectedExercise == null) {
            presenter.addNewExercise(createExerciseFromInput(), workoutId);
        } else {
            presenter.addExistingExercise(selectedExercise, workoutId);
        }
    }

    private Exercise createExerciseFromInput() {
        String newWeight = weight.getText().toString();
        if (newWeight.equals("")) newWeight = "0";

        return new Exercise(
                name.getText().toString(),
                repRange.getText().toString(),
                Double.parseDouble(newWeight));
    }

    @Override
    public void showEmptyInputError() {
        if (name.getText().toString().equals("")) showEmptyNameError();
        if (repRange.getText().toString().equals("")) showEmptyRangeError();
    }

    private void showEmptyNameError() {
        TextInputLayout name = findViewById(R.id.input_layout_name);
        name.setError(getString(R.string.error_empty_name));
    }

    private void showEmptyRangeError() {
        TextInputLayout range = findViewById(R.id.input_layout_rep_range);
        range.setError(getString(R.string.error_empty_rep_range));
    }

    @Override
    public void showMyExerciseList() {
        Intent intent = new Intent(this, SelectExerciseFromListActivity.class);
        startActivityForResult(intent, SELECT_EXERCISE_REQ);
    }

    @Override
    public void showWarningDialog(final long exerciseId, final long workoutId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setView(R.layout.dialog_add_exercise_warning);
        builder.setTitle("Exercise already in workout");
        builder.setMessage(R.string.dialog_exercise_warning);
        builder.setPositiveButton(R.string.button_add, (which, i) ->
                presenter.addExerciseToListAndFinish(exerciseId, workoutId));
        builder.setNegativeButton(R.string.button_cancel, (which, i) -> presenter.reset());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showWorkout() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showWorkoutWithError() {
        setResult(ExerciseListFragment.ADD_DUPLICATE_ERROR_CODE);
        finish();
    }

    @Override
    public void reset() {
        clearInputs();
        selectedExercise = null;
    }

    private void clearInputs() {
        name.setText("");
        repRange.setText("");
        weight.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
