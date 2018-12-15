package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sleepybear.mymoviecatalogue.BuildConfig;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.movie_title)
    TextView txtMovieTitle;

    @BindView(R.id.thumbnail)
    ImageView imgThumbnail;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Result result) {
        txtMovieTitle.setText(result.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + result.getPosterPath())
                .into(imgThumbnail);
    }
}
