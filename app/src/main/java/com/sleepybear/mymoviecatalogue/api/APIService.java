package com.sleepybear.mymoviecatalogue.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


import com.sleepybear.mymoviecatalogue.models.detail.DetailMovieModel;
import com.sleepybear.mymoviecatalogue.models.popular.PopularMovieModel;
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.models.trending.TrendingMovieModel;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingMovieModel;
import com.sleepybear.mymoviecatalogue.models.similiar.SimiliarMovieModel;

public interface APIService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @GET("movie/upcoming")
    Call<UpcomingMovieModel> getUpcomingMovie();

    @GET("movie/popular")
    Call<PopularMovieModel> getPopularMovie(@Query(value = "page") int page);

    @GET("trending/all/day")
    Call<TrendingMovieModel> getTrendingMovie();

    @GET("search/movie")
    Call<SearchMovieModel> searchMovie(@Query(value = "query") String query,
                                       @Query(value = "include_adult") boolean include_adult);

    @GET("movie/{movie_id}/similar")
    Call<SimiliarMovieModel> getSimiliarMovie(@Path(value = "movie_id") String movie_id);

    @GET("movie/{movie_id}")
    Call<DetailMovieModel> getDetailMovie(@Path(value = "movie_id") String movie_id);
}
