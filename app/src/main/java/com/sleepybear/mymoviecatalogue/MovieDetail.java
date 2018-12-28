package com.sleepybear.mymoviecatalogue;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sleepybear.mymoviecatalogue.db.DbContract;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.utils.AppPreferences;
import com.sleepybear.mymoviecatalogue.widget.FavoriteMoviesWidget;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetail extends AppCompatActivity {
    public static final String MOVIE_RESULT = "movie_result";
    private final Gson gson = new Gson();
    @Nullable
    @BindView(R.id.tv_movie_title)
    TextView movieTitle;
    @Nullable
    @BindView(R.id.tv_overview_movie)
    TextView movieDescription;
    @Nullable
    @BindView(R.id.tv_movie_rating)
    TextView movieRating;
    @Nullable
    @BindView(R.id.tv_release_date)
    TextView movieReleaseDate;
    @Nullable
    @BindView(R.id.iv_backdrop_poster)
    ImageView movieBackdrop;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.fab_favorite)
    FloatingActionButton fab_favorite;
    @Nullable
    @BindView(R.id.fab_share)
    FloatingActionButton fab_share;
    private AppPreferences appPreferences;
    private Result result;
    @NonNull
    private ArrayList<Result> dataFromDB = new ArrayList<>();
    private MovieDBHelper movieDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        appPreferences = new AppPreferences(this);
        movieDBHelper = new MovieDBHelper(MovieDetail.this);
        result = new Result();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Objects.requireNonNull(toolbar).setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Objects.requireNonNull(fab_favorite).setOnClickListener(view -> {
            if (!appPreferences.isFavorite()) {
                Objects.requireNonNull(fab_favorite).setColorFilter(Color.RED);
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
                Objects.requireNonNull(fab_favorite).setColorFilter(Color.BLACK);
                movieDBHelper.open();
                movieDBHelper.deleteFavorite(result.getOriginalTitle());
                movieDBHelper.close();
            }

            // update stack widget
            Context context = getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, FavoriteMoviesWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

        });

        Objects.requireNonNull(fab_share).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg, result.getOriginalTitle(), result.getVoteAverage()));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_button_text)));
        });

        String json = getIntent().getStringExtra(MOVIE_RESULT);
        result = gson.fromJson(json, Result.class);
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
        Objects.requireNonNull(movieTitle).setText(result.getOriginalTitle());
        Objects.requireNonNull(movieDescription).setText(result.getOverview());
        Objects.requireNonNull(movieRating).setText(getString(R.string.txtRate, result.getVoteAverage()));
        Objects.requireNonNull(movieReleaseDate).setText(getString(R.string.txtReleaseDate, result.getReleaseDate()));

        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + result.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_gray_24dp)
                        .error(R.drawable.ic_image_gray_24dp)
                        .centerCrop())
                .into(Objects.requireNonNull(movieBackdrop));

        Objects.requireNonNull(getSupportActionBar()).setTitle(result.getOriginalTitle());
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