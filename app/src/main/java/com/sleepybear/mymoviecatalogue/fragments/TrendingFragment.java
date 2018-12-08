package com.sleepybear.mymoviecatalogue.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Rect;
import android.content.res.Resources;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.TrendingAdapter;
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingMovieModel;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TrendingAdapter mAdapter;
    @BindView(R.id.trending_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    public TrendingFragment() {
        // Required empty public constructor
    }

    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this,view);
        mAdapter = new TrendingAdapter();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);
                fetchTrendingMovieItems();
            }
        });



        return view;
    }
    private void fetchTrendingMovieItems() {
        swipeRefreshLayout.setRefreshing(true);
        APIService service = NetworkInstance.getRetrofitInstance().create(APIService.class);
        Call<TrendingMovieModel> trendingMovieModelCall = service.getTrendingMovie();
        trendingMovieModelCall.enqueue(new Callback<TrendingMovieModel>() {
            @Override
            public void onResponse(Call<TrendingMovieModel> call, Response<TrendingMovieModel> response) {
                if(response.isSuccessful()){
                    int totalpage = response.body().getTotalPages();
                    Log.d("ROS",String.valueOf(totalpage));
                    List<TrendingResult> items = response.body().getTrendingResults();
                    mAdapter.clearAll();
                    mAdapter.updateData(items);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<TrendingMovieModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onRefresh() {
        fetchTrendingMovieItems();
    }
}
