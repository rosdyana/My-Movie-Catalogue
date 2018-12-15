package com.sleepybear.mymoviecatalogue.db;

import android.provider.BaseColumns;

public class DbContract {
    public static String TABLE_FAVORITE = "table_favorite";

    public static final class FavoriteColumns implements BaseColumns {
        public static String COL_ID = "movie_id";
        public static String COL_NAME = "name";
        public static String COL_POSTER_PATH = "poster_path";
        public static String COL_BACKDROP_PATH = "backdrop_path";
        public static String COL_OVERVIEW = "overview";
        public static String COL_RELEASE_DATE = "release_date";
        public static String COL_VOTE_AVG = "vote_average";
    }
}


