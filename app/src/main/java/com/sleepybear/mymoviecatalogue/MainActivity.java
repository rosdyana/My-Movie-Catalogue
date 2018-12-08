package com.sleepybear.mymoviecatalogue;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ConfigurationCompat;
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
import android.os.Handler;

import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.SearchFragment;
import com.sleepybear.mymoviecatalogue.fragments.TrendingFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.utils.ActivityUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
                mToolbarTitle.setText("Search");
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
                        mToolbarTitle.setText("Popular");
                        fragment = new PopularFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_now_playing:
                        mToolbarTitle.setText("Now Playing");
                        fragment = new NowPlayingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_trending:
                        mToolbarTitle.setText("Trending");
                        fragment = new TrendingFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_upcoming:
                        mToolbarTitle.setText("Upcoming");
                        fragment = new UpcomingFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        Log.d("ROS ", fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.action_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
