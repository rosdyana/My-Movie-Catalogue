package mymoviecatalog.sleepybear.com.favoritemovieapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import mymoviecatalog.sleepybear.com.favoritemovieapp.adapter.FavoriteMovieAdapter;
import mymoviecatalog.sleepybear.com.favoritemovieapp.db.DbContract;
import mymoviecatalog.sleepybear.com.favoritemovieapp.model.MovieItem;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final int LOAD_FAV_MOVIES_ID = 1;
    @Nullable
    @BindView(R.id.lv_movies)
    ListView lv_movies;
    private FavoriteMovieAdapter favoriteMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        favoriteMovieAdapter = new FavoriteMovieAdapter(this, null, true);
        Objects.requireNonNull(lv_movies).setAdapter(favoriteMovieAdapter);
        lv_movies.setOnItemClickListener(this);

        ContentResolver cr = getContentResolver();
        listEntries(cr);

    }

    private void listEntries(ContentResolver cr) {
        Uri uri = Uri.parse("content://" + DbContract.CONTENT_AUTHORITY + "/" + DbContract.TABLE_FAVORITE);
        Cursor cursor = cr.query(uri, null, null, null, null);

        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst()) {
            MovieItem movieItem = new MovieItem(cursor);
        }
        cursor.close();
    }


    @Override
    public void onItemClick(@NonNull AdapterView<?> adapterView, @NonNull View view, int i, long l) {
        TextView myTextView = view.findViewById(R.id.movie_title);
        Snackbar.make(adapterView, myTextView.getText().toString(), Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, DbContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        favoriteMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favoriteMovieAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_FAV_MOVIES_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAV_MOVIES_ID);
    }
}
