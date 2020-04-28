package com.ilham.made.fifthsubmission.webservices

import com.ilham.made.fifthsubmission.BuildConfig
import com.ilham.made.fifthsubmission.models.MovieCrewModel
import com.ilham.made.fifthsubmission.models.MovieModel
import com.ilham.made.fifthsubmission.models.TvShowModel
import com.ilham.made.fifthsubmission.utils.WrappedListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<WrappedListResponse<MovieModel>>

    @GET("movie/{movie_id}")
    fun getMovieInformation(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<MovieModel>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<MovieCrewModel>

    @GET("tv/top_rated")
    fun getTopRatedTvShow(
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<WrappedListResponse<TvShowModel>>

    @GET("tv/{tv_id}")
    fun getTvShowInformation(
        @Path("tv_id") tvId: Int,
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY
    ): Call<TvShowModel>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY,
        @Query("query") movie: String
    ): Call<WrappedListResponse<MovieModel>>

    @GET("search/tv")
    fun searchTvShow(
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY,
        @Query("query") tvShow: String
    ): Call<WrappedListResponse<TvShowModel>>

    @GET("discover/movie")
    fun getMovieReleaseToday(
        @Query("api_key") api_key: String = BuildConfig.TMDB_API_KEY,
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.lte") releaseDateLte: String
    ): Call<WrappedListResponse<MovieModel>>
}