package com.sleepybear.mymoviecatalogue.db;

import android.provider.BaseColumns;

public class DbContract {
    static String TABLE_FAVORITE = "table_favorite";

    static final class FavoriteColumns implements BaseColumns {
        static String COL_ID = "id";
        static String COL_NAME = "name";
        static String COL_POSTER_PATH = "poster_path";
        static String COL_BACKDROP_PATH = "backdrop_path";
        static String COL_OVERVIEW = "overview";
        static String COL_RELEASE_DATE = "release_date";
        static String COL_VOTE_AVG = "vote_average";
    }
}
