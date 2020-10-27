package com.maciej.software.nev2.ui.ExerciseSummary;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maciej.software.nev2.R;

public class ExerciseSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        initToolbar();
        initExerciseSummaryFragment();
    }

    private void initToolbar() {
        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_exercise_summary);
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_search, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initExerciseSummaryFragment() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.summary_list_frame, ExerciseSummaryFragment.newInstance());
        tx.commit();
    }
}
