package com.sleepybear.mymoviecatalogue.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sleepybear.mymoviecatalogue.MovieDetail;
import com.sleepybear.mymoviecatalogue.R;
import com.sleepybear.mymoviecatalogue.adapter.MovieAdapter;
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.listener.RecycleTouchListener;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingModel;
import com.sleepybear.mymoviecatalogue.utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NowPlayingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    MovieAdapter mAdapter;
    private List<Result> list = new ArrayList<>();
    @BindView(R.id.rv_recycler_view)
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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MovieAdapter();
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

        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getActivity(), recyclerView, new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Result obj = list.get(position);
//                Log.d("ROS",obj.toString());
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

    private void fetchNowplayingItems() {
        swipeRefreshLayout.setRefreshing(true);
        APIService apiService = NetworkInstance.getRetrofitInstance().create(APIService.class);
        String currentLanguage = utils.getDeviceLang(Locale.getDefault().getDisplayLanguage());
        Call<NowplayingModel> nowplayingModelCall = apiService.getNowplaying(currentLanguage);
        nowplayingModelCall.enqueue(new Callback<NowplayingModel>() {
            @Override
            public void onResponse(Call<NowplayingModel> call, Response<NowplayingModel> response) {
                if (response.isSuccessful()) {
                    List<Result> items = response.body().getResults();
                    list.addAll(items);
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
