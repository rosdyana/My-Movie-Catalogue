package com.sleepybear.mymoviecatalogue.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


import com.sleepybear.mymoviecatalogue.models.genre.GenreModel;
import com.sleepybear.mymoviecatalogue.models.nowplaying.NowplayingModel;
import com.sleepybear.mymoviecatalogue.models.popular.PopularMovieModel;
import com.sleepybear.mymoviecatalogue.models.search.SearchMovieModel;
import com.sleepybear.mymoviecatalogue.models.upcoming.UpcomingMovieModel;

public interface APIService {

    @GET("movie/upcoming")
    Call<UpcomingMovieModel> getUpcomingMovie(@Query(value = "language") String language);

    @GET("movie/popular")
    Call<PopularMovieModel> getPopularMovie(@Query(value = "language") String language);

    @GET("search/movie")
    Call<SearchMovieModel> searchMovie(@Query(value = "query") String query,
                                       @Query(value = "language") String language);

    @GET("movie/now_playing")
    Call<NowplayingModel> getNowplaying(@Query(value = "language") String language);

    @GET("genre/movie/list")
    Call<GenreModel> getGenre(@Query(value = "language") String language);
}
