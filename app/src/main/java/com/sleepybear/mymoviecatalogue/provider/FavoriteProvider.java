package com.sleepybear.mymoviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sleepybear.mymoviecatalogue.db.DbContract;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;

import java.util.Objects;

public class FavoriteProvider extends ContentProvider {
    private static final int MOVIE_FAVORITE_ID = 1;
    private static final int MOVIE_FAVORITE = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.TABLE_FAVORITE, MOVIE_FAVORITE);
        uriMatcher.addURI(DbContract.CONTENT_AUTHORITY, DbContract.TABLE_FAVORITE + "/#", MOVIE_FAVORITE_ID);
    }

    @Nullable
    private MovieDBHelper movieDBHelper;

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        movieDBHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAVORITE_ID:
                cursor = Objects.requireNonNull(movieDBHelper).queryByIdProvider(uri.getLastPathSegment());
                break;
            case MOVIE_FAVORITE:
                cursor = Objects.requireNonNull(movieDBHelper).queryProvider();
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null) {
            if (getContext() != null) {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long addedUri;

        switch (uriMatcher.match(uri)) {
            case MOVIE_FAVORITE:
                addedUri = Objects.requireNonNull(movieDBHelper).insertProvider(contentValues);
                break;
            default:
                addedUri = 0;
                break;
        }

        if (addedUri > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return Uri.parse(DbContract.CONTENT_URI + "/" + addedUri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deletedUri;
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAVORITE_ID:
                deletedUri = Objects.requireNonNull(movieDBHelper).deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deletedUri = 0;
                break;
        }

        if (deletedUri > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return deletedUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updatedUri;
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAVORITE_ID:
                updatedUri = Objects.requireNonNull(movieDBHelper).updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updatedUri = 0;
                break;
        }

        if (updatedUri > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return updatedUri;
    }
}
