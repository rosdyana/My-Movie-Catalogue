package com.sleepybear.mymoviecatalogue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sleepybear.mymoviecatalogue.BuildConfig;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;

import butterknife.BindView;
import butterknife.ButterKnife;

class TrendingViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.movie_title)
    TextView txtMovieTitle;

    @BindView(R.id.thumbnail)
    ImageView imgThumbnail;

    public TrendingViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final TrendingResult item){
        txtMovieTitle.setText(item.getTitle());
        Glide.with(itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG+item.getPosterPath())
                .into(imgThumbnail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
