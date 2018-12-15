package com.sleepybear.mymoviecatalogue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.SearchFragment;
import com.sleepybear.mymoviecatalogue.fragments.TrendingFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;
import com.sleepybear.mymoviecatalogue.models.popular.PopularResult;
import com.sleepybear.mymoviecatalogue.models.search.SearchResult;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingResult;
import com.sleepybear.mymoviecatalogue.utils.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
//    @BindView(R.id.thumbnail_poster)
//    ImageView moviePoster;
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
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ADD TO FAVORITE/UNFOVORITE", Snackbar.LENGTH_SHORT).show();
            }
        });

        String fragmentName = getIntent().getExtras().getString(FRAGMENT_NAME);
        Log.d("ROS", fragmentName);
        if (fragmentName.equals(TrendingFragment.class.getSimpleName())) {
            TrendingResult trendingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(trendingResult);
        } else if (fragmentName.equals(NowPlayingFragment.class.getSimpleName())) {
            NowplayingResult nowplayingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(nowplayingResult);
        } else if (fragmentName.equals(PopularFragment.class.getSimpleName())) {
            PopularResult popularResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(popularResult);
        } else if (fragmentName.equals(UpcomingFragment.class.getSimpleName())) {
            UpcomingResult upcomingResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(upcomingResult);
        } else if (fragmentName.equals(SearchFragment.class.getSimpleName())) {
            SearchResult searchResult = getIntent().getParcelableExtra(MOVIE_RESULT);
            loadData(searchResult);
        }

    }

    private void loadData(SearchResult searchResult) {
        movieTitle.setText(searchResult.getTitle());
        movieDescription.setText(searchResult.getOverview());
        movieRating.setText(getString(R.string.txtRate, searchResult.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, searchResult.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre,getGenre(searchResult.getGenreIds())));
//        Glide.with(getApplicationContext())
//                .load(BuildConfig.BASE_URL_IMG + searchResult.getPosterPath())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .error(R.drawable.ic_image_black_24dp)
//                        .centerCrop())
//                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + searchResult.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(searchResult.getTitle());

        addShareButton(getString(R.string.share_msg, searchResult.getTitle(), searchResult.getVoteAverage()));
    }


    private void loadData(UpcomingResult upcomingResult) {
        movieTitle.setText(upcomingResult.getTitle());
        movieDescription.setText(upcomingResult.getOverview());
        movieRating.setText(getString(R.string.txtRate, upcomingResult.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, upcomingResult.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre,getGenre(upcomingResult.getGenreIds())));
//        Glide.with(getApplicationContext())
//                .load(BuildConfig.BASE_URL_IMG + upcomingResult.getPosterPath())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .error(R.drawable.ic_image_black_24dp)
//                        .centerCrop())
//                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + upcomingResult.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(upcomingResult.getTitle());

        addShareButton(getString(R.string.share_msg, upcomingResult.getTitle(), upcomingResult.getVoteAverage()));
    }

    private void loadData(PopularResult popularResult) {
        movieTitle.setText(popularResult.getTitle());
        movieDescription.setText(popularResult.getOverview());
        movieRating.setText(getString(R.string.txtRate, popularResult.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, popularResult.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre,getGenre(popularResult.getGenreIds())));
//        Glide.with(getApplicationContext())
//                .load(BuildConfig.BASE_URL_IMG + popularResult.getPosterPath())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .error(R.drawable.ic_image_black_24dp)
//                        .centerCrop())
//                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + popularResult.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(popularResult.getTitle());

        addShareButton(getString(R.string.share_msg, popularResult.getTitle(), popularResult.getVoteAverage()));
    }

    private void loadData(NowplayingResult nowplayingResult) {
        movieTitle.setText(nowplayingResult.getTitle());
        movieDescription.setText(nowplayingResult.getOverview());
        movieRating.setText(getString(R.string.txtRate, nowplayingResult.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, nowplayingResult.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre,getGenre(nowplayingResult.getGenreIds())));
//        Glide.with(getApplicationContext())
//                .load(BuildConfig.BASE_URL_IMG + nowplayingResult.getPosterPath())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .error(R.drawable.ic_image_black_24dp)
//                        .centerCrop())
//                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + nowplayingResult.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(nowplayingResult.getTitle());

        addShareButton(getString(R.string.share_msg, nowplayingResult.getTitle(), nowplayingResult.getVoteAverage()));
    }

    private void loadData(TrendingResult trendingResult) {
        movieTitle.setText(trendingResult.getTitle().isEmpty() ? trendingResult.getName() : trendingResult.getTitle());
        movieDescription.setText(trendingResult.getOverview());
        movieRating.setText(getString(R.string.txtRate, trendingResult.getVoteAverage()));
        movieReleaseDate.setText(getString(R.string.txtReleaseDate, trendingResult.getReleaseDate()));
        movieGenres.setText(getString(R.string.txtGenre,getGenre(trendingResult.getGenreIds())));
//        Glide.with(getApplicationContext())
//                .load(BuildConfig.BASE_URL_IMG + trendingResult.getPosterPath())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .error(R.drawable.ic_image_black_24dp)
//                        .centerCrop())
//                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(BuildConfig.BASE_URL_IMG_BACKDROP + trendingResult.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop())
                .into(movieBackdrop);

        getSupportActionBar().setTitle(trendingResult.getTitle().isEmpty() ? trendingResult.getName() : trendingResult.getTitle());

        addShareButton(getString(R.string.share_msg, trendingResult.getTitle().isEmpty() ? trendingResult.getName() : trendingResult.getTitle(), trendingResult.getVoteAverage()));
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

    private void addShareButton(final String message) {
        btnFacebbok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFB(message);
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTwitter(message);
            }
        });
    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + utils.urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, getString(R.string.twitter_app_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFB(String message) {
        Intent fbIntent = new Intent(Intent.ACTION_SEND);
        fbIntent.putExtra(Intent.EXTRA_TEXT, message);
        fbIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(fbIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")) {
                fbIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(fbIntent);
        } else {
            Toast.makeText(this, getString(R.string.fb_app_not_found), Toast.LENGTH_SHORT).show();
        }
    }




}