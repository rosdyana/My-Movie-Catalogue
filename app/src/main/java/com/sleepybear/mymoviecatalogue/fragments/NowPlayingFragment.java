package com.sleepybear.mymoviecatalogue.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.NowplayingAdapter;
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingModel;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NowPlayingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    NowplayingAdapter mAdapter;
    @BindView(R.id.nowplaying_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new NowplayingAdapter();
        RecyclerView.LayoutManager mlayLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(mlayLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchNowplayingItems();
            }
        });
        return view;
    }

    private void fetchNowplayingItems() {
        swipeRefreshLayout.setRefreshing(true);
        APIService apiService = NetworkInstance.getRetrofitInstance().create(APIService.class);
        Call<NowplayingModel> nowplayingModelCall = apiService.getNowplaying();
        nowplayingModelCall.enqueue(new Callback<NowplayingModel>() {
            @Override
            public void onResponse(Call<NowplayingModel> call, Response<NowplayingModel> response) {
                if(response.isSuccessful()){
                    List<NowplayingResult> items = response.body().getResults();
                    mAdapter.clearAll();
                    mAdapter.updateData(items);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<NowplayingModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchNowplayingItems();
    }
}
