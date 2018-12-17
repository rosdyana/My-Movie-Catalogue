package mymoviecatalog.sleepybear.com.favoritemovieapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import mymoviecatalog.sleepybear.com.favoritemovieapp.BuildConfig;
import mymoviecatalog.sleepybear.com.favoritemovieapp.R;
import mymoviecatalog.sleepybear.com.favoritemovieapp.db.DbContract;

public class FavoriteMovieAdapter extends CursorAdapter {
    @BindView(R.id.movie_title)
    TextView tv_movie_title;
    @BindView(R.id.release_date)
    TextView tv_relese_date;
    @BindView(R.id.overview_movie)
    TextView tv_overview_movie;
    @BindView(R.id.thumbnail_poster)
    ImageView im_thumbnail_poster;

    public FavoriteMovieAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_row, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor != null){
            ButterKnife.bind(this, view);
            tv_movie_title.setText(DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_NAME));
            tv_overview_movie.setText(DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_OVERVIEW));
            tv_relese_date.setText(DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_RELEASE_DATE));
            Glide.with(view.getContext())
                    .load(BuildConfig.BASE_URL_IMG + DbContract.getColumnString(cursor, DbContract.FavoriteColumns.COL_POSTER_PATH))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_image_black_24dp)
                            .error(R.drawable.ic_image_black_24dp)
                            .centerCrop())
                    .into(im_thumbnail_poster);
        }
    }
}
