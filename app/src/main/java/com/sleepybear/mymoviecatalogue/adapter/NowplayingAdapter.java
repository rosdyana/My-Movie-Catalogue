package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;

import java.util.ArrayList;
import java.util.List;

public class NowplayingAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<NowplayingResult> list = new ArrayList<>();

    public NowplayingAdapter(){

    }

    public void updateData(List<NowplayingResult> items){
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void clearAll(){
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MovieViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder( MovieViewHolder holder, int i) {
        holder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
