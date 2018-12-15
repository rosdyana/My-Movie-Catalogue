package com.sleepybear.mymoviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;

public class MovieDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MovieDBHelper(Context context) {
        this.context = context;
    }

    public MovieDBHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }


    public ArrayList<Result> getValueByMovieId(Integer MovieId) {
        ArrayList<Result> arrayList = new ArrayList<>();
        Result result;
        Cursor cursor = sqLiteDatabase.query(DbContract.TABLE_FAVORITE,
                null,
                DbContract.FavoriteColumns.COL_ID + " = ?",
                new String[]{String.valueOf(MovieId)}, null, null, null);


        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                result = new Result();
                result.setId(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_ID));
                result.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_NAME)));
                result.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_BACKDROP_PATH)));
                result.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_POSTER_PATH)));
                result.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_OVERVIEW)));
                result.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_RELEASE_DATE)));
                result.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_VOTE_AVG)));

                arrayList.add(result);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Result result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.FavoriteColumns.COL_ID, result.getId());
        contentValues.put(DbContract.FavoriteColumns.COL_NAME, result.getTitle());
        contentValues.put(DbContract.FavoriteColumns.COL_OVERVIEW, result.getOverview());
        contentValues.put(DbContract.FavoriteColumns.COL_BACKDROP_PATH, result.getBackdropPath());
        contentValues.put(DbContract.FavoriteColumns.COL_POSTER_PATH, result.getPosterPath());
        contentValues.put(DbContract.FavoriteColumns.COL_RELEASE_DATE, result.getReleaseDate());
        contentValues.put(DbContract.FavoriteColumns.COL_VOTE_AVG, result.getVoteAverage());
        return sqLiteDatabase.insert(DbContract.TABLE_FAVORITE, null, contentValues);
    }


    public int delete(int id) {
        return sqLiteDatabase.delete(DbContract.TABLE_FAVORITE, DbContract.FavoriteColumns.COL_ID + " = '" + id + "'", null);
    }
}
