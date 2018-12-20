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
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String searchQuery;
    MovieAdapter mAdapter;
    private List<Result> list = new ArrayList<>();
    @BindView(R.id.rv_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchQuery = this.getArguments().getString("search_query");
//        Log.d("ROS search ", searchQuery);
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MovieAdapter();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchSearchMovieItems();
            }
        });

        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getActivity(), recyclerView, new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Result obj = list.get(position);
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

    private void fetchSearchMovieItems() {
        swipeRefreshLayout.setRefreshing(true);
        APIService service = NetworkInstance.getRetrofitInstance().create(APIService.class);
        String currentLanguage = utils.getDeviceLang(Locale.getDefault().getDisplayLanguage());
        Call<SearchMovieModel> searchMovieModelCall = service.searchMovie(searchQuery, currentLanguage);
        searchMovieModelCall.enqueue(new Callback<SearchMovieModel>() {
            @Override
            public void onResponse(Call<SearchMovieModel> call, Response<SearchMovieModel> response) {
                if (response.isSuccessful()) {
                    List<Result> items = response.body().getSearchResults();
                    list.addAll(items);
                    mAdapter.clearAll();
                    mAdapter.updateData(items);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<SearchMovieModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onRefresh() {

    }
}
