package com.maciej.software.nev2.ui.EditExercise;

import com.maciej.software.nev2.model.Exercise;

public interface EditExercise {

    interface View {

        void showEmptyInputError();
        void showExerciseList();
    }

    interface Presenter {

        void editExercise(Exercise exercise, String name, String repRange, String weight);
    }
}
