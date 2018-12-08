package com.sleepybear.mymoviecatalogue.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingModel;
import com.sleepybear.mymoviecatalogue.models.popular.PopularMovieModel;
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingMovieModel;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingMovieModel;

public interface APIService {

    @GET("movie/upcoming")
    Call<UpcomingMovieModel> getUpcomingMovie();

    @GET("movie/popular")
    Call<PopularMovieModel> getPopularMovie();

    @GET("trending/all/day")
    Call<TrendingMovieModel> getTrendingMovie();

    @GET("search/movie")
    Call<SearchMovieModel> searchMovie(@Query(value = "query") String query,
                                       @Query(value = "include_adult") boolean include_adult);

    @GET("movie/now_playing")
    Call<NowplayingModel> getNowplaying();
}
