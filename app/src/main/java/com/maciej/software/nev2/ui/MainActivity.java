package com.maciej.software.nev2.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.ActivityMainBinding;
import com.maciej.software.nev2.ui.MoreOptions.MoreOptionsFragment;
import com.maciej.software.nev2.ui.WorkoutContainerFragment.WorkoutFragment;


public class MainActivity extends AppCompatActivity {

    private final int TOOLBAR_SHADOW = 4;
    private final int TOOLBAR_NO_SHADOW = 0;

    private ActivityMainBinding binding;
    private Toolbar appBar;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);

        appBar = (Toolbar) binding.appToolbar;
        setSupportActionBar(appBar);
        setAppBarElevation(true);
        initFirstFrag();

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment newFragment = null;
            switch (item.getItemId()) {
                case R.id.intensity:
                    newFragment = WorkoutFragment.newInstance(getString(R.string.workout_type_intensity));
                    setAppBarElevation(true);
                    break;

                case R.id.volume:
                    newFragment = WorkoutFragment.newInstance(getString(R.string.workout_type_volume));
                    setAppBarElevation(true);
                    break;

                case R.id.more_options:
                    newFragment = MoreOptionsFragment.newInstance();
                    setAppBarElevation(false);
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_frame, newFragment);
            transaction.commit();
            return true;
        });
    }

    /**
     * Set the toolbar title and shadow based on the tab you're in
     * @param setDefault If this is the default tab (intensity or volume)
     */
    private void setAppBarElevation(boolean setDefault) {
        if (setDefault) {
            setElevation(TOOLBAR_NO_SHADOW);
            appBar.setTitle(R.string.app_name);
        } else {
            setElevation(TOOLBAR_SHADOW);
            appBar.setTitle(R.string.title_more_options);
        }
    }

    private void setElevation(int value) {
        appBar.setElevation(value);
    }

    private void initFirstFrag() {
        Fragment workoutFrag = WorkoutFragment.newInstance("Intensity");
        FragmentTransaction initialTransaction = getSupportFragmentManager().beginTransaction();
        initialTransaction.replace(R.id.activity_frame, workoutFrag).commit();
    }
}
