package com.sleepybear.mymoviecatalogue;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sleepybear.mymoviecatalogue.db.DbContract;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetail extends AppCompatActivity {
    public static final String MOVIE_RESULT = "movie_result";
    private AppPreferences appPreferences;
    private Result result;
    private ArrayList<Result> dataFromDB = new ArrayList<>();
    private MovieDBHelper movieDBHelper;
    @BindView(R.id.tv_movie_title)
    TextView movieTitle;
    @BindView(R.id.tv_overview_movie)
    TextView movieDescription;
    @BindView(R.id.tv_movie_rating)
    TextView movieRating;
    @BindView(R.id.tv_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.tv_movie_genres)
    TextView movieGenres;
    @BindView(R.id.iv_backdrop_poster)
    ImageView movieBackdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_favorite)
    FloatingActionButton fab_favorite;
    @BindView(R.id.fab_share)
    FloatingActionButton fab_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        appPreferences = new AppPreferences(this);
        movieDBHelper = new MovieDBHelper(MovieDetail.this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appPreferences.isFavorite()) {
                    fab_favorite.setColorFilter(Color.RED);
                    appPreferences.setFavorite(true);
                    ContentValues cv = new ContentValues();
                    cv.put(DbContract.FavoriteColumns.COL_MOVIE_ID, result.getId());
                    cv.put(DbContract.FavoriteColumns.COL_NAME, result.getOriginalTitle());
                    cv.put(DbContract.FavoriteColumns.COL_OVERVIEW, result.getOverview());
                    cv.put(DbContract.FavoriteColumns.COL_BACKDROP_PATH, result.getBackdropPath());
                    cv.put(DbContract.FavoriteColumns.COL_POSTER_PATH, result.getPosterPath());
                    cv.put(DbContract.FavoriteColumns.COL_RELEASE_DATE, result.getReleaseDate());
                    cv.put(DbContract.FavoriteColumns.COL_VOTE_AVG, result.getVoteAverage());
                    getContentResolver().insert(DbContract.CONTENT_URI, cv);

                } else {
                    appPreferences.setFavorite(false);
                    fab_favorite.setColorFilter(Color.BLACK);
//                    getContentResolver().delete(Uri.parse(DbContract.CONTENT_URI + "/" + result.getId()),null,null);
                    movieDBHelper.open();
                    movieDBHelper.deleteFavorite(result.getOriginalTitle());
                    movieDBHelper.close();
                }

            }
        });

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg, result.getOriginalTitle(), result.getVoteAverage()));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_button_text)));
            }
        });

        result = getIntent().getParcelableExtra(MOVIE_RESULT);
        Log.d("ROS",result.toString());
        try {
            movieDBHelper.open();
            dataFromDB = movieDBHelper.getMovieByName(result.getOriginalTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            movieDBHelper.close();
        }

        if (!dataFromDB.isEmpty()) {
            if (dataFromDB.get(0).getOriginalTitle().equals(result.getOriginalTitle())) {
                fab_favorite.setColorFilter(Color.RED);
                appPreferences.setFavorite(true);
            } else {
                fab_favorite.setColorFilter(Color.BLACK);
                appPreferences.setFavorite(false);
            }
        } else {
            fab_favorite.setColorFilter(Color.BLACK);
            appPreferences.setFavorite(false);
        }


        loadData(result);

    }

    private void loadData(Result result) {
        movieTitle.setText(result.getOriginalTitle());
        movieDescription.setText(result.getOverview());
        movieRating.setText(getString(R.string.txtRate, result.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, result.getReleaseDate()));
        if(getGenre(result.getGenreIds()).isEmpty()){
            movieGenres.setVisibility(View.GONE);
        } else {
            movieGenres.setText(getString(R.string.txtGenre, getGenre(result.getGenreIds())));
        }


        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + result.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_gray_24dp)
                        .error(R.drawable.ic_image_gray_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(result.getOriginalTitle());
    }

    private String getGenre(List<Integer> genreIds) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < MainActivity.ourMovieGenres.size(); i++) {
            if (genreIds.contains(MainActivity.ourMovieGenres.get(i).getId())) {
                result.add(MainActivity.ourMovieGenres.get(i).getName());
            }
        }
//        Log.d("ROS", TextUtils.join(",", result));
        return TextUtils.join(" , ", result);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}