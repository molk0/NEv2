package com.maciej.software.nev2.ui.Measurements.History;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasurementsHistoryFragment extends Fragment {


    public MeasurementsHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements_history, container, false);
    }

}
