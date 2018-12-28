package com.sleepybear.mymoviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMoviesWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.sleepybear.mymoviecatalogue.TOAST_ACTION";
    private List<Result> list = new ArrayList<>();
    private Gson gson = new Gson();

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movies_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);

        views.setEmptyView(R.id.stack_view, R.id.empty_view);
        views.setTextViewText(R.id.banner_text, widgetText);


        Intent toastIntent = new Intent(context, FavoriteMoviesWidget.class);

        toastIntent.setAction(FavoriteMoviesWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TOAST_ACTION)) {
            String json = intent.getStringExtra(MovieDetail.MOVIE_RESULT);
            Result result = gson.fromJson(json, Result.class);
            String movieName = result.getOriginalTitle();
            Toast.makeText(context, movieName, Toast.LENGTH_SHORT).show();
            Intent detailmovie = new Intent(context, MovieDetail.class);
            detailmovie.setAction(FavoriteMoviesWidget.TOAST_ACTION);
            detailmovie.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(result));
            context.startActivity(detailmovie);
        }
        super.onReceive(context, intent);
    }
}

