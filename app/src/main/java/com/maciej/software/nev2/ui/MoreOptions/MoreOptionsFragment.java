package com.maciej.software.nev2.ui.MoreOptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.FragmentMoreOptionsBinding;
import com.maciej.software.nev2.ui.AboutActivity;
import com.maciej.software.nev2.ui.ExerciseSummary.ExerciseSummaryActivity;
import com.maciej.software.nev2.ui.ExtraWorkouts.ExtraWorkoutActivity;
import com.maciej.software.nev2.ui.Measurements.MeasurementsActivity;


public class MoreOptionsFragment extends Fragment {

    FragmentMoreOptionsBinding binding;

    public MoreOptionsFragment() {    }

    public static MoreOptionsFragment newInstance() {
        return new MoreOptionsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_more_options, container, false);
        binding = FragmentMoreOptionsBinding.bind(contentView);

        binding.moreWorkouts.setOnClickListener(l -> showExtraWorkoutActivity());
        binding.list.setOnClickListener(l -> showExerciseSummaryActivity());
        //binding.measurements.setOnClickListener(l -> showMeasurementsActivity());
        binding.about.setOnClickListener(l -> showAboutActivity());

        return contentView;
    }

    private void showExtraWorkoutActivity() {
        Intent intent = new Intent(getContext(), ExtraWorkoutActivity.class);
        startActivity(intent);
    }

    private void showExerciseSummaryActivity() {
        Intent intent = new Intent(getContext(), ExerciseSummaryActivity.class);
        startActivity(intent);
    }

    private void showMeasurementsActivity() {
        Intent intent = new Intent(getContext(), MeasurementsActivity.class);
        startActivity(intent);
    }

    private void showAboutActivity() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }
}
