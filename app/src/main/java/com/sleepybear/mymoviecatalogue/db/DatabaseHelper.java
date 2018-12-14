package com.sleepybear.mymoviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "moviedb";

    private static final int DB_VERSION = 1;

    public static String CREATE_TABLE_FAVORITE = "create table " + DbContract.TABLE_FAVORITE
            + " (" + DbContract.FavoriteColumns.COL_ID + " integer primary key autoincrement, "
            + DbContract.FavoriteColumns.COL_BACKDROP_PATH + " text not null, "
            + DbContract.FavoriteColumns.COL_POSTER_PATH + " text not null, "
            + DbContract.FavoriteColumns.COL_NAME + " text not null, "
            + DbContract.FavoriteColumns.COL_OVERVIEW + " text not null, "
            + DbContract.FavoriteColumns.COL_RELEASE_DATE + " text not null, "
            + DbContract.FavoriteColumns.COL_VOTE_AVG + " text not null);";

    public static String DROP_TABLE_IF_EXIST = "DROP TABLE IF EXISTS " + DbContract.TABLE_FAVORITE+");";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_IF_EXIST);
        onCreate(sqLiteDatabase);
    }
}
