package com.sleepybear.mymoviecatalogue.fragments;

import android.content.Intent;
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
import com.sleepybear.mymoviecatalogue.api.APIService;
import com.sleepybear.mymoviecatalogue.api.NetworkInstance;
import com.sleepybear.mymoviecatalogue.listener.RecycleTouchListener;
import com.sleepybear.mymoviecatalogue.models.Result;
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String STATE_SAVE = "state_save";
    @Nullable
    @BindView(R.id.rv_recycler_view)
    RecyclerView recyclerView;
    @Nullable
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter mAdapter;
    @Nullable
    private String searchQuery;
    @Nullable
    private ArrayList<Result> list = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchQuery = Objects.requireNonNull(this.getArguments()).getString("search_query");
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MovieAdapter();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        Objects.requireNonNull(recyclerView).setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        Objects.requireNonNull(swipeRefreshLayout).setOnRefreshListener(this);

        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(STATE_SAVE);
            mAdapter.updateData(Objects.requireNonNull(list));
        } else {
            swipeRefreshLayout.post(() -> {
                swipeRefreshLayout.setRefreshing(true);
                fetchSearchMovieItems();
            });
        }


        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getActivity(), position -> {
            Result obj = list.get(position);
            Intent intent = new Intent(getActivity(), MovieDetail.class);
            intent.putExtra(MovieDetail.MOVIE_RESULT, new Gson().toJson(obj));
            startActivity(intent);
        }));
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SAVE, list);
    }

    private void fetchSearchMovieItems() {
        Objects.requireNonNull(swipeRefreshLayout).setRefreshing(true);
        APIService service = NetworkInstance.getRetrofitInstance().create(APIService.class);
        String currentLanguage = Utils.getDeviceLang(Locale.getDefault().getDisplayLanguage());
        Call<SearchMovieModel> searchMovieModelCall = service.searchMovie(searchQuery, currentLanguage);
        searchMovieModelCall.enqueue(new Callback<SearchMovieModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchMovieModel> call, @NonNull Response<SearchMovieModel> response) {
                if (response.isSuccessful()) {
                    List<Result> items = Objects.requireNonNull(response.body()).getSearchResults();
                    Objects.requireNonNull(list).addAll(items);
                    mAdapter.clearAll();
                    mAdapter.updateData(Objects.requireNonNull(items));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchMovieModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onRefresh() {

    }
}
