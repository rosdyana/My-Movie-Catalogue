package com.sleepybear.mymoviecatalogue.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {
    public static final String CONTENT_AUTHORITY = "com.sleepybear.mymoviecatalogue";
    public static final String TABLE_FAVORITE = "table_favorite";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content").authority(CONTENT_AUTHORITY)
            .appendPath(DbContract.TABLE_FAVORITE).build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public static final class FavoriteColumns implements BaseColumns {
        public static final String COL_MOVIE_ID = "_id";
        public static final String COL_NAME = "name";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_BACKDROP_PATH = "backdrop_path";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_VOTE_AVG = "vote_average";
    }
}
