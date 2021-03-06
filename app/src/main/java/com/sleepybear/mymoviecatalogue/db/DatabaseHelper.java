package com.sleepybear.mymoviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";

    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_FAVORITE = "create table " + DbContract.TABLE_FAVORITE
            + " (" + DbContract.FavoriteColumns.COL_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , "
            + DbContract.FavoriteColumns.COL_NAME + " text not null, "
            + DbContract.FavoriteColumns.COL_OVERVIEW + " text not null, "
            + DbContract.FavoriteColumns.COL_BACKDROP_PATH + " text not null, "
            + DbContract.FavoriteColumns.COL_POSTER_PATH + " text not null, "
            + DbContract.FavoriteColumns.COL_RELEASE_DATE + " text not null, "
            + DbContract.FavoriteColumns.COL_VOTE_AVG + " double not null);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.TABLE_FAVORITE);
        onCreate(sqLiteDatabase);
    }
}
