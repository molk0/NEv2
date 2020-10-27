package com.maciej.software.nev2.ui.WorkoutContainerFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.FragmentWorkoutBinding;
import com.maciej.software.nev2.ui.ExerciseListFragment.ExerciseListFragment;

public class WorkoutFragment extends Fragment {

    private static final String WORKOUT_TYPE_KEY = "version";

    private String workoutType;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(@NonNull String workoutType) {
        Bundle extras = new Bundle();
        extras.putString(WORKOUT_TYPE_KEY, workoutType);
        WorkoutFragment frag = new WorkoutFragment();
        frag.setArguments(extras);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            workoutType = getArguments().getString(WORKOUT_TYPE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView =  inflater.inflate(R.layout.fragment_workout, container, false);
        FragmentWorkoutBinding workoutBinding = FragmentWorkoutBinding.bind(contentView);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Fragment fragA = ExerciseListFragment.newInstance(getString(R.string.workout_week1), workoutType);
        Fragment fragB = ExerciseListFragment.newInstance(getString(R.string.workout_week2), workoutType);
        Fragment fragC = ExerciseListFragment.newInstance(getString(R.string.workout_week3), workoutType);
        Fragment fragD = ExerciseListFragment.newInstance(getString(R.string.workout_week4), workoutType);
        adapter.addFragment(fragA, getString(R.string.workout_week1));
        adapter.addFragment(fragB, getString(R.string.workout_week2));
        adapter.addFragment(fragC, getString(R.string.workout_week3));
        adapter.addFragment(fragD, getString(R.string.workout_week4));

        ViewPager pager = workoutBinding.viewPager;
        pager.setAdapter(adapter);
        workoutBinding.tabs.setupWithViewPager(pager);

        return contentView;
    }

}
