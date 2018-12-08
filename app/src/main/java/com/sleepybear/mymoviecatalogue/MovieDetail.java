package com.sleepybear.mymoviecatalogue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.TrendingFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;
import com.sleepybear.mymoviecatalogue.models.popular.PopularResult;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {
    public static final String MOVIE_RESULT = "movie_result";
    public static final String FRAGMENT_NAME = "fragment_name";
    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.overview_movie)
    TextView movieDescription;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.thumbnail_poster)
    ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        String fragmentName = getIntent().getExtras().getString(FRAGMENT_NAME);
        Log.d("ROS",fragmentName);
        if(fragmentName.equals(TrendingFragment.class.getSimpleName())){
            TrendingResult trendingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(trendingResult);
        }
        else if(fragmentName.equals(NowPlayingFragment.class.getSimpleName())){
            NowplayingResult nowplayingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(nowplayingResult);
        } else if (fragmentName.equals(PopularFragment.class.getSimpleName())){
            PopularResult popularResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(popularResult);
        } else if (fragmentName.equals(UpcomingFragment.class.getSimpleName())){
            UpcomingResult upcomingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(upcomingResult);
        }


    }

    private void loadData(UpcomingResult upcomingResult) {
        movieTitle.setText(upcomingResult.getTitle());
        movieDescription.setText(upcomingResult.getOverview());
        movieRating.setText(upcomingResult.getVoteAverage().toString());
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG + upcomingResult.getPosterPath())
                .into(moviePoster);

        getSupportActionBar().setTitle(upcomingResult.getTitle());
    }

    private void loadData(PopularResult popularResult) {
        movieTitle.setText(popularResult.getTitle());
        movieDescription.setText(popularResult.getOverview());
        movieRating.setText(popularResult.getVoteAverage().toString());
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG + popularResult.getPosterPath())
                .into(moviePoster);

        getSupportActionBar().setTitle(popularResult.getTitle());
    }

    private void loadData(NowplayingResult nowplayingResult) {
        movieTitle.setText(nowplayingResult.getTitle());
        movieDescription.setText(nowplayingResult.getOverview());
        movieRating.setText(nowplayingResult.getVoteAverage().toString());
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG + nowplayingResult.getPosterPath())
                .into(moviePoster);

        getSupportActionBar().setTitle(nowplayingResult.getTitle());
    }

    private void loadData(TrendingResult trendingResult) {
        movieTitle.setText(trendingResult.getTitle().isEmpty() ? trendingResult.getName() : trendingResult.getTitle());
        movieDescription.setText(trendingResult.getOverview());
        movieRating.setText(trendingResult.getVoteAverage().toString());
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG + trendingResult.getPosterPath())
                .into(moviePoster);

        getSupportActionBar().setTitle(trendingResult.getTitle().isEmpty() ? trendingResult.getName() : trendingResult.getTitle());
    }
}