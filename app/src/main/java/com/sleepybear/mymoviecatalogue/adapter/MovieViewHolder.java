package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sleepybear.mymoviecatalogue.BuildConfig;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;
import com.sleepybear.mymoviecatalogue.models.popular.PopularResult;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingResult;

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

    public void bind(TrendingResult trendingResult) {
        txtMovieTitle.setText(trendingResult.getTitle() == null ? trendingResult.getName() : trendingResult.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + trendingResult.getPosterPath())
                .into(imgThumbnail);
    }

    public void bind(UpcomingResult upcomingResult) {
        txtMovieTitle.setText(upcomingResult.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + upcomingResult.getPosterPath())
                .into(imgThumbnail);
    }

    public void bind(PopularResult popularResult) {
        txtMovieTitle.setText(popularResult.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + popularResult.getPosterPath())
                .into(imgThumbnail);
    }

    public void bind(NowplayingResult nowplayingResult) {
        txtMovieTitle.setText(nowplayingResult.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + nowplayingResult.getPosterPath())
                .into(imgThumbnail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
