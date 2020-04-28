package com.ilham.made.consumerfavorite.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ilham.made.consumerfavorite.BuildConfig
import com.ilham.made.consumerfavorite.data.db.movie.MovieDatabase
import com.ilham.made.consumerfavorite.data.db.movie.asMovieModel
import com.ilham.made.consumerfavorite.models.MovieCrewModel
import com.ilham.made.consumerfavorite.models.MovieModel
import com.ilham.made.consumerfavorite.models.asMovieDatabaseEntity
import com.ilham.made.consumerfavorite.utils.SingleLiveEvent
import com.ilham.made.consumerfavorite.utils.WrappedListResponse
import com.ilham.made.consumerfavorite.webservices.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MovieRepository(private val database: MovieDatabase) {

    companion object {
        private val TAG = MovieRepository::class.java.simpleName
    }

    val movies: LiveData<List<MovieModel>> = Transformations.map(database.movieDao.getMovies()) {
        it.asMovieModel()
    }

    private var state: SingleLiveEvent<MovieState> = SingleLiveEvent()

    suspend fun resultMovieSearch(query: String): List<MovieModel>? {
        state.value = MovieState.IsLoading(true)
        try {
            val response = ApiClient.instance.searchMovie(BuildConfig.TMDB_API_KEY, query).awaitResponse()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    state.value = MovieState.IsLoading(false)
                    return response.body()!!.results
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ERROR : ${e.message}")
            state.postValue(MovieState.Error("Cannot connect to the server"))
        }

        return null
    }

    fun refreshMovies() {
        state.value = MovieState.IsLoading(true)
        ApiClient.instance.getNowPlayingMovies().enqueue(object : Callback<WrappedListResponse<MovieModel>>{
            override fun onFailure(call: Call<WrappedListResponse<MovieModel>>, t: Throwable) {
                state.value = MovieState.Error("Cannot connect to the server")
            }

            override fun onResponse(
                call: Call<WrappedListResponse<MovieModel>>,
                response: Response<WrappedListResponse<MovieModel>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body()!!.results?.asMovieDatabaseEntity()
                        if (body != null) {
                            GlobalScope.launch(Dispatchers.IO) {
                                database.movieDao.insertAllMovies(body)
                            }
                            state.value = MovieState.IsSuccess(true)
                        }
                    }
                } else {
                    state.value = MovieState.Error("Failed to get results")
                }

                state.value = MovieState.IsLoading(false)
            }

        })
    }

    suspend fun getMovieDetail(movieId: Int) : MovieModel? {
        state.value = MovieState.IsLoading(true)
        try {
            val movieDetail = ApiClient.instance.getMovieInformation(movieId).awaitResponse()
            if (movieDetail.isSuccessful) {
                if (movieDetail.body() != null) {
                    Log.d(TAG, "data" + movieDetail.body().toString())
                    state.value = MovieState.IsLoading(false)
                    return movieDetail.body()!!
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ERROR : ${e.message}")
            state.postValue(MovieState.Error("Cannot connect to the server"))
        }

        return null
    }

    suspend fun getMovieCrew(movieId: Int) : MovieCrewModel? {
        try {
            val movieCrew = ApiClient.instance.getMovieCredits(movieId).awaitResponse()
            if (movieCrew.isSuccessful) {
                if (movieCrew.body() != null) {
                    Log.d(TAG, "data" + movieCrew.body().toString())
                    return movieCrew.body()!!
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ERROR : ${e.message}")
            state.postValue(MovieState.Error("Cannot connect to the server"))
        }

        return null
    }

    fun getMovieState() = state

}

sealed class MovieState {
    data class IsLoading(var state: Boolean = false) : MovieState()
    data class Error(var error: String?) : MovieState()
    data class IsSuccess(var success: Boolean = false) : MovieState()
}

