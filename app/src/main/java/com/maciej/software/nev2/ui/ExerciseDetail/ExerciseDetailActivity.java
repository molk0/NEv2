package com.maciej.software.nev2.ui.ExerciseDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.ExerciseListFragment.ExerciseListFragment;

public class ExerciseDetailActivity extends AppCompatActivity {

    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        getExtras();
        initToolbar();
        initExerciseDetailFragment();
    }

    private void getExtras() {
        Intent intent = getIntent();
        exercise = intent.getParcelableExtra(ExerciseListFragment.EXERCISE_KEY);
    }

    private void initToolbar() {
        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_detail);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initExerciseDetailFragment() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.detail_content_frame, ExerciseDetailFragment.newInstance(exercise));
        tx.commit();
    }
}
