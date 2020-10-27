package com.maciej.software.nev2.ui.ExerciseListFragment;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.RowExerciseBinding;
import com.maciej.software.nev2.model.Exercise;

import java.util.Collections;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.
        ExerciseViewHolder> {

    private List<Exercise> mExercises;
    private ExerciseList.Presenter mPresenter;


    public ExerciseAdapter(@NonNull List<Exercise> exercises, @NonNull ExerciseList.Presenter presenter) {
        setList(exercises);
        mPresenter = presenter;
    }

    public void replaceData(@NonNull final List<Exercise> exercises) {
        setList(exercises);
        notifyDataSetChanged();
    }

    public void clearData() {
        mExercises.clear();
        notifyDataSetChanged();
    }

    public Exercise getItem(int position) {
        return mExercises.get(position);
    }

    public void onItemMove(int fromPos, int toPos) {
        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(mExercises, i, i+1);
            }
        } else {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(mExercises, i, i-1);
            }
        }
        notifyItemMoved(fromPos, toPos);
    }

    private void setList(final List<Exercise> exercises) {
        mExercises = exercises;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowExerciseBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_exercise,
                parent, false);
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ExerciseAdapter.ExerciseViewHolder holder, int position) {

        Exercise currentExercise = mExercises.get(position);
        holder.bind(currentExercise);

        holder.binding.menuIbtn.setOnClickListener(view ->
                mPresenter.loadBottomSheetMenu(mExercises.get(position)));
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private RowExerciseBinding binding;

        public ExerciseViewHolder(RowExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull final Exercise exercise) {
            binding.setExercise(exercise);
            binding.setPresenter(mPresenter);
            binding.executePendingBindings();
        }
    }
}
