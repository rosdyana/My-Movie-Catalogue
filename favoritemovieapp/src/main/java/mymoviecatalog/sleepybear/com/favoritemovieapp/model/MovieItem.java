package mymoviecatalog.sleepybear.com.favoritemovieapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import mymoviecatalog.sleepybear.com.favoritemovieapp.db.DbContract;

public class MovieItem implements Parcelable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getThumbnail_poster() {
        return thumbnail_poster;
    }

    public void setThumbnail_poster(String thumbnail_poster) {
        this.thumbnail_poster = thumbnail_poster;
    }

    private String movie_title, movie_overview, release_date, thumbnail_poster;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movie_title);
        dest.writeString(this.movie_overview);
        dest.writeString(this.release_date);
        dest.writeString(this.thumbnail_poster);
    }

    public MovieItem() {
    }

    public MovieItem(Cursor cursor) {
        this.id = DbContract.getColumnInt(cursor, DbContract.FavoriteColumns._ID);
        this.movie_title = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_NAME);
        this.movie_overview = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_OVERVIEW);
        this.thumbnail_poster = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_POSTER_PATH);
        this.release_date = DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_RELEASE_DATE);
    }

    protected MovieItem(Parcel in) {
        this.id = in.readInt();
        this.movie_title = in.readString();
        this.movie_overview = in.readString();
        this.release_date = in.readString();
        this.thumbnail_poster = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
