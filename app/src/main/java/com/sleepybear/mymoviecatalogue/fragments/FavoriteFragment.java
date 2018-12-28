package com.sleepybear.mymoviecatalogue.fragments;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.MovieAdapter;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;
import com.sleepybear.mymoviecatalogue.listener.RecycleTouchListener;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String STATE_SAVE = "state_save";
    @Nullable
    @BindView(R.id.rv_recycler_view)
    RecyclerView recyclerView;
    @Nullable
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter mAdapter;
    @Nullable
    private ArrayList<Result> results = new ArrayList<>();
    @Nullable
    private MovieDBHelper movieDBHelper;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MovieAdapter();
        movieDBHelper = new MovieDBHelper(getContext());
        RecyclerView.LayoutManager mlayLayoutManager = new GridLayoutManager(getActivity(), 2);

        Objects.requireNonNull(recyclerView).setLayoutManager(mlayLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        Objects.requireNonNull(swipeRefreshLayout).setOnRefreshListener(this);

        if (savedInstanceState != null) {
            results = savedInstanceState.getParcelableArrayList(STATE_SAVE);
            mAdapter.updateData(Objects.requireNonNull(results));
        } else {
            swipeRefreshLayout.post(() -> {
                swipeRefreshLayout.setRefreshing(true);
                loadFromDB();
            });
        }


        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getActivity(), position -> {
            Result obj = results.get(position);
//                Log.d("ROS click", obj.getId().toString());
            Intent intent = new Intent(getActivity(), MovieDetail.class);
            intent.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(obj));
            startActivity(intent);
        }));
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SAVE, results);
    }

    private void loadFromDB() {
        Objects.requireNonNull(swipeRefreshLayout).setRefreshing(true);
        try {
            Objects.requireNonNull(movieDBHelper).open();
            results = movieDBHelper.getAllData();
            mAdapter.clearAll();
            mAdapter.updateData(results);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            movieDBHelper.close();
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        loadFromDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromDB();
    }

}
