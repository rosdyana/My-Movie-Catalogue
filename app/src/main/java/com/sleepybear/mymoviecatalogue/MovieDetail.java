package com.sleepybear.mymoviecatalogue;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetail extends AppCompatActivity {
    public static final String MOVIE_RESULT = "movie_result";
    public static final String FRAGMENT_NAME = "fragment_name";
    private AppPreferences appPreferences;
    private ContentValues contentValues;
    private Result result;
    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.overview_movie)
    TextView movieDescription;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.release_date)
    TextView movieReleaseDate;
    @BindView(R.id.movie_genres)
    TextView movieGenres;
    @BindView(R.id.twitter_btn)
    ImageButton btnTwitter;
    @BindView(R.id.facebook_btn)
    ImageButton btnFacebbok;
    @BindView(R.id.backdrop_poster)
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
        contentValues = new ContentValues();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appPreferences.isFavorite()) {
                    fab_favorite.setColorFilter(Color.RED);
//                    contentValues.put(DbContract.FavoriteColumns.COL_ID, )
                    appPreferences.setFavorite(true);
                } else {
                    appPreferences.setFavorite(false);
                    fab_favorite.setColorFilter(Color.GRAY);
                }

            }
        });

        fab_favorite.setColorFilter(Color.GRAY);
        appPreferences.setFavorite(false);

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg, result.getTitle(), result.getVoteAverage()));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_button_text)));
            }
        });

        String fragmentName = getIntent().getExtras().getString(FRAGMENT_NAME);
        Log.d("ROS", fragmentName);
        result = getIntent().getParcelableExtra(MOVIE_RESULT);
        loadData(result);

    }

    private void loadData(Result result) {
        movieTitle.setText(result.getTitle());
        movieDescription.setText(result.getOverview());
        movieRating.setText(getString(R.string.txtRate, result.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, result.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre, getGenre(result.getGenreIds())));

        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + result.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_gray_24dp)
                        .error(R.drawable.ic_image_gray_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(result.getTitle());
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
}