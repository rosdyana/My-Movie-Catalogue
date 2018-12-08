package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingResult;

import java.util.ArrayList;
import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<UpcomingResult> list = new ArrayList<>();

    public UpcomingAdapter() {

    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void updateData(List<UpcomingResult> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
