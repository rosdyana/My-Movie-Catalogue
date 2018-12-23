package com.sleepybear.mymoviecatalogue.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.sleepybear.mymoviecatalogue.BuildConfig;
import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private ArrayList<Result> results = new ArrayList<>();
    private MovieDBHelper movieDBHelper;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        movieDBHelper = new MovieDBHelper(mContext);
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    private void loadData() {
        try {
            movieDBHelper.open();
            results = movieDBHelper.getAllData();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            movieDBHelper.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
//        Log.d("ROS", results.toString());
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(BuildConfig.BASE_URL_IMG_BACKDROP + results.get(position).getBackdropPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        String movieTitle = results.get(position).getOriginalTitle();

        rv.setImageViewBitmap(R.id.iv_backdrop_poster, bitmap);
        rv.setTextViewText(R.id.tv_movie_title, movieTitle);

//        Bundle extras = new Bundle();
//        extras.putString(FavoriteMoviesWidget.EXTRA_ITEM_TITLE, movieTitle);
//        extras.putString(FavoriteMoviesWidget.EXTRA_ITEM_BACKDROP, results.get(position).getBackdropPath());
//        extras.putString(FavoriteMoviesWidget.EXTRA_ITEM_OVERVIEW, results.get(position).getOverview());
//        extras.putString(FavoriteMoviesWidget.EXTRA_ITEM_RELEASE_DATE, results.get(position).getReleaseDate());
//        extras.putDouble(FavoriteMoviesWidget.EXTRA_ITEM_RATING, results.get(position).getVoteAverage());
//        extras.putIntegerArrayList(FavoriteMoviesWidget.EXTRA_ITEM_GENRE, results.get(position).getGenreIds());
//        extras.putInt(FavoriteMoviesWidget.EXTRA_ITEM_IDX, position);
        Intent fillInIntent = new Intent();
        Result itemresult = results.get(position);
        fillInIntent.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(itemresult));
        rv.setOnClickFillInIntent(R.id.iv_backdrop_poster, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}