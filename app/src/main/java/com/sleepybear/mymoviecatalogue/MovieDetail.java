package com.sleepybear.mymoviecatalogue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {
    public static final String MOVIE_RESULT = "movie_result";
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

        String movie_detail = getIntent().getParcelableExtra(MOVIE_RESULT);

        loadData(movie_detail);

    }

    private void loadData(String movie_detail) {

    }
}