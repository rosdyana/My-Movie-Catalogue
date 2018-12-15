package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Result> resultList = new ArrayList<>();

    public MovieAdapter() {

    }

    public void updateData(List<Result> items) {
        resultList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearAll() {
        resultList.clear();
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MovieViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(resultList.get(i));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}