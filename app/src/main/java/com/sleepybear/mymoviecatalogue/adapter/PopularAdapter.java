package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.popular.PopularResult;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<PopularResult> popularResultList = new ArrayList<>();

    public PopularAdapter() {

    }

    public void updateData(List<PopularResult> items) {
        popularResultList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearAll() {
        popularResultList.clear();
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MovieViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(popularResultList.get(i));
    }

    @Override
    public int getItemCount() {
        return popularResultList.size();
    }
}
