package mymoviecatalog.sleepybear.com.favoritemovieapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import mymoviecatalog.sleepybear.com.favoritemovieapp.db.DbContract;

public class MovieItem implements Parcelable {
    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(@NonNull Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
    private final int id;
    private final String movie_title;
    private final String movie_overview;
    private final String release_date;
    private final String thumbnail_poster;

    public MovieItem(@NonNull Cursor cursor) {
        this.id = DbContract.getColumnInt(cursor, DbContract.FavoriteColumns.COL_MOVIE_ID);
        this.movie_title = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_NAME);
        this.movie_overview = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_OVERVIEW);
        this.thumbnail_poster = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_POSTER_PATH);
        this.release_date = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_RELEASE_DATE);
    }

    private MovieItem(Parcel in) {
        this.id = in.readInt();
        this.movie_title = in.readString();
        this.movie_overview = in.readString();
        this.release_date = in.readString();
        this.thumbnail_poster = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movie_title);
        dest.writeString(this.movie_overview);
        dest.writeString(this.release_date);
        dest.writeString(this.thumbnail_poster);
    }

    @NonNull
    public String toString() {
        return "\n" + id + "\n" + movie_title;
    }
}
