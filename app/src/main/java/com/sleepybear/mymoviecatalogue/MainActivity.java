package com.sleepybear.mymoviecatalogue;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sleepybear.mymoviecatalogue.fragments.FavoriteFragment;
import com.sleepybear.mymoviecatalogue.fragments.NowPlayingFragment;
import com.sleepybear.mymoviecatalogue.fragments.PopularFragment;
import com.sleepybear.mymoviecatalogue.fragments.SearchFragment;
import com.sleepybear.mymoviecatalogue.fragments.UpcomingFragment;
import com.sleepybear.mymoviecatalogue.utils.ActivityUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String FRAGMENT_STATE = "fragment_state";
    private static final String FRAGMENT_NAME = "fragment_name";
    @Nullable
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNav;
    private SearchView searchView;
    private Integer fragmentState;
    @Nullable
    private String fragmentName;
    private Fragment fragment;
    private TextView mToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActivityUtils.setStatusBarGradiant(MainActivity.this, getResources().getDrawable(R.drawable.header_gradient_color));
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
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


        ImageButton mBtnSetting = view.findViewById(R.id.ib_action_setting_menu);
        mBtnSetting.setOnClickListener(view1 -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
        });

        mToolbarTitle = view.findViewById(R.id.tv_toolbar_title);

        if (savedInstanceState != null) {
            fragmentState = savedInstanceState.getInt(FRAGMENT_STATE);
            fragmentName = savedInstanceState.getString(FRAGMENT_NAME);
            mToolbarTitle.setText(fragmentName);
            view = Objects.requireNonNull(mBottomNav).findViewById(fragmentState);
            view.performClick();
        } else {
            mToolbarTitle.setText(getString(R.string.text_popular));
            loadFragment(new PopularFragment());
            fragmentState = R.id.action_popular;
            fragmentName = getString(R.string.text_popular);
        }
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        Objects.requireNonNull(mBottomNav).setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_popular:
                    mToolbarTitle.setText(getString(R.string.text_popular));
                    fragment = new PopularFragment();
                    loadFragment(fragment);
                    fragmentState = R.id.action_popular;
                    fragmentName = getString(R.string.text_popular);
                    break;
                case R.id.action_now_playing:
                    mToolbarTitle.setText(getString(R.string.text_now_playing));
                    fragment = new NowPlayingFragment();
                    loadFragment(fragment);
                    fragmentState = R.id.action_now_playing;
                    fragmentName = getString(R.string.text_now_playing);
                    break;
                case R.id.action_upcoming:
                    mToolbarTitle.setText(getString(R.string.text_upcoming));
                    fragment = new UpcomingFragment();
                    loadFragment(fragment);
                    fragmentState = R.id.action_upcoming;
                    fragmentName = getString(R.string.text_upcoming);
                    break;
                case R.id.action_favorite:
                    mToolbarTitle.setText(getString(R.string.text_favorite));
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    fragmentState = R.id.action_favorite;
                    fragmentName = getString(R.string.text_favorite);
                    break;
            }
            return true;
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_STATE, fragmentState);
        outState.putString(FRAGMENT_NAME, fragmentName);
    }

    private void loadFragment(@NonNull final Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(mBottomNav).getSelectedItemId() == R.id.action_popular) {
            super.onBackPressed();
            finish();
        } else {
            // always back to first menu
            mBottomNav.setSelectedItemId(R.id.action_popular);
        }
    }

}
