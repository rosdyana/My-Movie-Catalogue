package com.sleepybear.mymoviecatalogue.api;

import android.support.annotation.NonNull;

import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingModel;
import com.sleepybear.mymoviecatalogue.models.popular.PopularMovieModel;
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingMovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @NonNull
    @GET("movie/upcoming")
    Call<UpcomingMovieModel> getUpcomingMovie(@Query(value = "language") String language);

    @NonNull
    @GET("movie/popular")
    Call<PopularMovieModel> getPopularMovie(@Query(value = "language") String language);

    @NonNull
    @GET("search/movie")
    Call<SearchMovieModel> searchMovie(@Query(value = "query") String query,
                                       @Query(value = "language") String language);

    @NonNull
    @GET("movie/now_playing")
    Call<NowplayingModel> getNowplaying(@Query(value = "language") String language);

}
