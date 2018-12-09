package com.sleepybear.mymoviecatalogue;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.SearchFragment;
import com.sleepybear.mymoviecatalogue.fragments.TrendingFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.models.genre.Genre;
import com.sleepybear.mymoviecatalogue.models.genre.GenreModel;
import com.sleepybear.mymoviecatalogue.utils.ActivityUtils;
import com.sleepybear.mymoviecatalogue.utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static List<Genre> ourMovieGenres = new ArrayList<>();
    private Fragment fragment;
    private TextView mToolbarTitle;
    SearchView searchView;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getGenreFromServer();
        ActivityUtils.setStatusBarGradiant(MainActivity.this, getResources().getDrawable(R.drawable.header_gradient_color));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.basic_custom_action_bar_dark_layout);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header_gradient_color));

        View view = getSupportActionBar().getCustomView();

        searchView = view.findViewById(R.id.searchView);
        ImageView icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
                mToolbarTitle.setText(getString(R.string.text_search));
                Bundle bundle = new Bundle();
                bundle.putString("search_query", s);
                fragment = new SearchFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        ImageButton mBtnSetting = view.findViewById(R.id.action_setting_menu);
        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }
        });

        mToolbarTitle = view.findViewById(R.id.toolbar_title);

        mToolbarTitle.setText(getString(R.string.text_trending));
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
        loadFragment(new TrendingFragment());

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_popular:
                        mToolbarTitle.setText(getString(R.string.text_popular));
                        fragment = new PopularFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_now_playing:
                        mToolbarTitle.setText(getString(R.string.text_now_playing));
                        fragment = new NowPlayingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_trending:
                        mToolbarTitle.setText(getString(R.string.text_trending));
                        fragment = new TrendingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_upcoming:
                        mToolbarTitle.setText(getString(R.string.text_upcoming));
                        fragment = new UpcomingFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(final Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        Log.d("ROS ", fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mBottomNav.getSelectedItemId() == R.id.action_trending) {
            super.onBackPressed();
            finish();
        } else {
            // always back to first menu
            mBottomNav.setSelectedItemId(R.id.action_trending);
        }
    }


    public void getGenreFromServer() {
        APIService service = NetworkInstance.getRetrofitInstance().create(APIService.class);
        String currentLanguage = utils.getDeviceLang(Locale.getDefault().getDisplayLanguage());
        Call<GenreModel> genreModelCall = service.getGenre(currentLanguage);
        genreModelCall.enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                if (response.isSuccessful()) {
                    List<Genre> items = response.body().getGenres();
                    ourMovieGenres.clear();
                    ourMovieGenres.addAll(items);
                }
            }

            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {

            }
        });
    }
}
