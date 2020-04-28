package com.ilham.made.thirdsubmission.webservices

import com.ilham.made.thirdsubmission.BuildConfig
import com.ilham.made.thirdsubmission.models.MovieCrewModel
import com.ilham.made.thirdsubmission.models.MovieModel
import com.ilham.made.thirdsubmission.models.TvShowModel
import com.ilham.made.thirdsubmission.utils.WrappedListResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Get List Upcoming Movies
    @GET("movie/upcoming")
    fun fetchAllMovies(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<WrappedListResponses<MovieModel>>

    // Get Movie Detail
    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<MovieModel>

    // Get Movie Credits ( only crew )
    @GET("movie/{movie_id}/credits")
    fun fetchMovieCredits(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<MovieCrewModel>

    // Get List TV Show Top Rated
    @GET("tv/top_rated")
    fun fetchAllTvShow(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<WrappedListResponses<TvShowModel>>

    // Get TV Show Detail
    @GET("tv/{tv_id}")
    fun getDetailTv(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<TvShowModel>

}