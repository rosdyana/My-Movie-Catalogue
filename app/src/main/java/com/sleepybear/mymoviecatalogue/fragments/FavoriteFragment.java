package com.sleepybear.mymoviecatalogue.fragments;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.MovieAdapter;
import com.sleepybear.mymoviecatalogue.db.MovieDBHelper;
import com.sleepybear.mymoviecatalogue.listener.RecycleTouchListener;
import com.sleepybear.mymoviecatalogue.models.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    MovieAdapter mAdapter;
    private List<Result> results = new ArrayList<>();
    private MovieDBHelper movieDBHelper;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MovieAdapter();
        movieDBHelper = new MovieDBHelper(getContext());
        RecyclerView.LayoutManager mlayLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(mlayLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadFromDB();
            }
        });

        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getActivity(), recyclerView, new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Result obj = results.get(position);
//                Log.d("ROS click", obj.getId().toString());
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra(MovieDetail.MOVIE_RESULT, obj);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }


    private void loadFromDB() {
        swipeRefreshLayout.setRefreshing(true);
        try {
            movieDBHelper.open();
            results = movieDBHelper.getAllData();
            mAdapter.clearAll();
            mAdapter.updateData(results);
//            Log.d("ROS",results.toString());
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
    public void onResume(){
        super.onResume();
        loadFromDB();
    }
}
