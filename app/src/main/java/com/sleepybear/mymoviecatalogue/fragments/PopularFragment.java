package com.sleepybear.mymoviecatalogue.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.PopularAdapter;
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.models.popular.PopularMovieModel;
import com.sleepybear.mymoviecatalogue.models.popular.PopularResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    PopularAdapter mAdapter;
    @BindView(R.id.popular_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;


    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new PopularAdapter();
        RecyclerView.LayoutManager mlayLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(mlayLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchPopularMovieItems();
            }
        });
        return view;
    }

    private void fetchPopularMovieItems() {
        swipeRefreshLayout.setRefreshing(true);
        APIService apiService = NetworkInstance.getRetrofitInstance().create(APIService.class);
        Call<PopularMovieModel> popularMovieModelCall = apiService.getPopularMovie();
        popularMovieModelCall.enqueue(new Callback<PopularMovieModel>() {
            @Override
            public void onResponse(Call<PopularMovieModel> call, Response<PopularMovieModel> response) {
                if (response.isSuccessful()) {
                    List<PopularResult> popularResultList = response.body().getResults();
                    mAdapter.clearAll();
                    mAdapter.updateData(popularResultList);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<PopularMovieModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        fetchPopularMovieItems();
    }
}
