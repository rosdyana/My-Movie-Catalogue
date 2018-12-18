package com.sleepybear.mymoviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        Cursor cursor = sqLiteDatabase.query(DbContract.TABLE_FAVORITE, null, DbContract.FavoriteColumns.COL_MOVIE_ID + " = ?",
                new String[] { String.valueOf(MovieId) }, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                result = new Result();
                result.setId(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_MOVIE_ID));
                result.setOriginalTitle(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_NAME)));
                result.setBackdropPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_BACKDROP_PATH)));
                result.setPosterPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_POSTER_PATH)));
                result.setOverview(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_OVERVIEW)));
                result.setReleaseDate(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_RELEASE_DATE)));
                result.setVoteAverage(
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_VOTE_AVG)));

                arrayList.add(result);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Result> getMovieByName(String name) {
        ArrayList<Result> arrayList = new ArrayList<>();
        Result result;
        Cursor cursor = sqLiteDatabase.query(DbContract.TABLE_FAVORITE, null,
                DbContract.FavoriteColumns.COL_NAME + " LIKE ?", new String[] { name }, null, null,
                DbContract.FavoriteColumns.COL_MOVIE_ID + " ASC", null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                result = new Result();
                result.setId(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_MOVIE_ID));
                result.setOriginalTitle(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_NAME)));
                result.setBackdropPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_BACKDROP_PATH)));
                result.setPosterPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_POSTER_PATH)));
                result.setOverview(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_OVERVIEW)));
                result.setReleaseDate(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_RELEASE_DATE)));
                result.setVoteAverage(
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_VOTE_AVG)));

                arrayList.add(result);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryAllData() {
        return sqLiteDatabase.rawQuery(
                "SELECT * FROM " + DbContract.TABLE_FAVORITE + " ORDER BY " + DbContract.FavoriteColumns.COL_MOVIE_ID + " ASC",
                null);
    }

    public ArrayList<Result> getAllData() {
        ArrayList<Result> arrayList = new ArrayList<>();
        Result result;

        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                result = new Result();
                result.setId(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_MOVIE_ID));
                result.setOriginalTitle(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_NAME)));
                result.setBackdropPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_BACKDROP_PATH)));
                result.setPosterPath(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_POSTER_PATH)));
                result.setOverview(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_OVERVIEW)));
                result.setReleaseDate(
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_RELEASE_DATE)));
                result.setVoteAverage(
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.FavoriteColumns.COL_VOTE_AVG)));

                arrayList.add(result);
//                Log.d("ROS getalldata", result.toString());
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long addFavorite(Result result) {
//        Log.d("ROS INSERt",result.toString());
        ContentValues contentValues = new ContentValues();
//        contentValues.put(DbContract.FavoriteColumns._ID, result.getId());
        contentValues.put(DbContract.FavoriteColumns.COL_MOVIE_ID, result.getId());
        contentValues.put(DbContract.FavoriteColumns.COL_NAME, result.getOriginalTitle());
        contentValues.put(DbContract.FavoriteColumns.COL_OVERVIEW, result.getOverview());
        contentValues.put(DbContract.FavoriteColumns.COL_BACKDROP_PATH, result.getBackdropPath());
        contentValues.put(DbContract.FavoriteColumns.COL_POSTER_PATH, result.getPosterPath());
        contentValues.put(DbContract.FavoriteColumns.COL_RELEASE_DATE, result.getReleaseDate());
        contentValues.put(DbContract.FavoriteColumns.COL_VOTE_AVG, result.getVoteAverage());
        return sqLiteDatabase.insert(DbContract.TABLE_FAVORITE, null, contentValues);
    }

    public int deleteFavorite(String name) {
        return sqLiteDatabase.delete(DbContract.TABLE_FAVORITE, DbContract.FavoriteColumns.COL_NAME + " LIKE '" + name + "'",
                null);
    }

    public Cursor queryByIdProvider(String id) {
        return sqLiteDatabase.query(DbContract.TABLE_FAVORITE, null, DbContract.FavoriteColumns.COL_MOVIE_ID + " = ?",
                new String[] { id }, null, null, null, null);
    }

    public Cursor queryProvider() {
        return sqLiteDatabase.query(DbContract.TABLE_FAVORITE, null, null, null, null, null,
                DbContract.FavoriteColumns.COL_MOVIE_ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return sqLiteDatabase.insert(DbContract.TABLE_FAVORITE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return sqLiteDatabase.update(DbContract.TABLE_FAVORITE, values, DbContract.FavoriteColumns.COL_MOVIE_ID + " = ?",
                new String[] { id });
    }

    public int deleteProvider(String id) {
        return sqLiteDatabase.delete(DbContract.TABLE_FAVORITE, DbContract.FavoriteColumns.COL_MOVIE_ID + " = ?",
                new String[] { id });
    }

}
