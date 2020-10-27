package com.maciej.software.nev2.ui.DetailHistory;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.databinding.RowDetailHistoryBinding;
import com.maciej.software.nev2.model.Detail;

import java.util.Collections;
import java.util.List;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.DetailViewHolder> {

    private List<Detail> details;
    private DetailHistory.Presenter presenter;

    public DetailHistoryAdapter(@NonNull List<Detail> details,
                                @NonNull DetailHistory.Presenter presenter) {
        setList(details);
        this.presenter = presenter;
    }

    public void replaceData(@NonNull final List<Detail> details) {
        Collections.reverse(details);
        setList(details);
        notifyDataSetChanged();
    }

    public void clearData() {
        details.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        details.remove(position);
        notifyItemRemoved(position);
    }

    public Detail getItem(int position) {
        return details.get(position);
    }

    public void restoreItem(Detail detail, int position) {
        details.add(position, detail);
        notifyItemInserted(position);
    }

    private void setList(final List<Detail> details) {
        this.details = details;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowDetailHistoryBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.row_detail_history, parent, false);
        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

        Detail currentDetail = details.get(position);
        holder.bind(currentDetail);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        private RowDetailHistoryBinding binding;
        public ConstraintLayout foreground, background;

        public DetailViewHolder(RowDetailHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            foreground = binding.viewForeground;
            background = binding.viewBackground;
        }

        public void bind(@NonNull final Detail detail) {
            binding.setDetail(detail);
            binding.setPresenter(presenter);
            binding.executePendingBindings();
        }
    }
}
