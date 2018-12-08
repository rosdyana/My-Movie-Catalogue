package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;

import java.util.ArrayList;
import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingViewHolder> {
    private List<TrendingResult> list = new ArrayList<>();

    public TrendingAdapter() {

    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public void updateData(List<TrendingResult> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public TrendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(TrendingViewHolder holder, int position){
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}
