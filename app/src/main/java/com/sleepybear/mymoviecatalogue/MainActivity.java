package com.sleepybear.mymoviecatalogue;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import android.widget.Toast;

import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.SearchFragment;
import com.sleepybear.mymoviecatalogue.fragments.TrendingFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.models.genre.Genre;
import com.sleepybear.mymoviecatalogue.models.genre.GenreModel;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;
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
    private final String FRAGMENT_OTHER = "FRAGMENT_OTHER";
    private final String FRAGMENT_HOME = "FRAGMENT_HOME";
    public static List<Genre> ourMovieGenres = new ArrayList<>();
    private List<String> fragmentNameList = new ArrayList<>();
    private Fragment fragment;
    private TextView mToolbarTitle;
    private boolean doubleBackToExitPressedOnce = false;
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
                loadFragment(fragment, FRAGMENT_OTHER);
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
        loadFragment(new TrendingFragment(), FRAGMENT_HOME);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_popular:
                        mToolbarTitle.setText(getString(R.string.text_popular));
                        fragment = new PopularFragment();
                        loadFragment(fragment, FRAGMENT_OTHER);
                        break;
                    case R.id.action_now_playing:
                        mToolbarTitle.setText(getString(R.string.text_now_playing));
                        fragment = new NowPlayingFragment();
                        loadFragment(fragment, FRAGMENT_OTHER);
                        break;
                    case R.id.action_trending:
                        mToolbarTitle.setText(getString(R.string.text_trending));
                        fragment = new TrendingFragment();
                        loadFragment(fragment, FRAGMENT_HOME);
                        break;
                    case R.id.action_upcoming:
                        mToolbarTitle.setText(getString(R.string.text_upcoming));
                        fragment = new UpcomingFragment();
                        loadFragment(fragment, FRAGMENT_OTHER);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(final Fragment fragment, String name) {
//        final FragmentManager fragmentManager = getFragmentManager();
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        Log.d("ROS ", fragment.getClass().getSimpleName());
//        final int count = fragmentManager.getBackStackEntryCount();
//        if(name.equals(FRAGMENT_OTHER)){
//            transaction.addToBackStack(FRAGMENT_OTHER);
//        }
        fragmentNameList.add(fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
//
//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if(fragmentManager.getBackStackEntryCount() <= count){
//                    fragmentManager.popBackStack(FRAGMENT_OTHER, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    fragmentManager.removeOnBackStackChangedListener(this);
//                    View view = mBottomNav.findViewById(R.id.action_trending);
//                    view.performClick();
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        fragmentNameList.remove(fragmentNameList.size()-1);
        if (mBottomNav.getSelectedItemId() == R.id.action_trending) {
            super.onBackPressed();
            finish();
        } else {
            // always back to first menu
            mBottomNav.setSelectedItemId(R.id.action_trending);
        }
//        super.onBackPressed();
//        String currentFragmentName = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
//        if(currentFragmentName.equals(TrendingFragment.class.getSimpleName())){
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        } else if (currentFragmentName.equals(PopularFragment.class.getSimpleName())){
//            mBottomNav.setSelectedItemId(R.id.action_popular);
//        } else if (currentFragmentName.equals(UpcomingFragment.class.getSimpleName())){
//            mBottomNav.setSelectedItemId(R.id.action_upcoming);
//        } else if (currentFragmentName.equals(NowPlayingFragment.class.getSimpleName())){
//            mBottomNav.setSelectedItemId(R.id.action_now_playing);
//        }
//        Log.d("ROS",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            android.support.v4.app.Fragment f =
//                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//            if (f instanceof TrendingFragment) {
//                mBottomNav.setSelectedItemId(R.id.action_trending);
//            } else if (f instanceof PopularFragment) {
//                mBottomNav.setSelectedItemId(R.id.action_popular);
//            } else if (f instanceof NowPlayingFragment) {
//                mBottomNav.setSelectedItemId(R.id.action_now_playing);
//            } else if (f instanceof UpcomingFragment){
//                mBottomNav.setSelectedItemId(R.id.action_upcoming);
//            }
//        }

//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, getString(R.string.action_exit), Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
    }

    private int getFragmentIdByName(String s) {
        if (s.equals("PopularFragment")) {
            return R.id.action_popular;
        } else if (s.equals("NowPlayingFragment")) {
            return R.id.action_now_playing;
        } else if (s.equals("UpcomingFragment")) {
            return R.id.action_upcoming;
        }
        return R.id.action_trending;
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
