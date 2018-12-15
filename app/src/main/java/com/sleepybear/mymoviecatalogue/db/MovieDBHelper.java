package com.sleepybear.mymoviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public Cursor queryByIdProvider(String id) {
        return sqLiteDatabase.query(DbContract.TABLE_FAVORITE, null,
                DbContract.FavoriteColumns.COL_ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public Cursor queryProvider() {
        return sqLiteDatabase.query(
                DbContract.TABLE_FAVORITE,
                null,
                null,
                null,
                null,
                null,
                null,
                DbContract.FavoriteColumns.COL_ID + " DESC"
        );
    }

    public long insertProvider(ContentValues contentValues) {
        return sqLiteDatabase.insert(
                DbContract.TABLE_FAVORITE,
                null,
                contentValues);
    }

    public int updateProvider(String id, ContentValues contentValues) {
        return sqLiteDatabase.update(
                DbContract.TABLE_FAVORITE,
                contentValues,
                DbContract.FavoriteColumns.COL_ID + " = ?",
                new String[]{id});
    }

    public int deleteProvider(String id) {
        return sqLiteDatabase.delete(
                DbContract.TABLE_FAVORITE,
                DbContract.FavoriteColumns.COL_ID + " = ?",
                new String[]{id});
    }
}
