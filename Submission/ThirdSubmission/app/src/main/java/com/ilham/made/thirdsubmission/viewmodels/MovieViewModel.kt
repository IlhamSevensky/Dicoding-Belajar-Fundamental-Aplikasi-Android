package com.ilham.made.thirdsubmission.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilham.made.thirdsubmission.BuildConfig
import com.ilham.made.thirdsubmission.models.MovieCrewModel
import com.ilham.made.thirdsubmission.models.MovieModel
import com.ilham.made.thirdsubmission.utils.Constants
import com.ilham.made.thirdsubmission.utils.SingleLiveEvent
import com.ilham.made.thirdsubmission.utils.WrappedListResponses
import com.ilham.made.thirdsubmission.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    /*
        MutableLiveData valuenya bisa diubah,
        LiveData valuenya tidak bisa diubah langsung
     */
    private var movies = MutableLiveData<List<MovieModel>>()
    private var movie = MutableLiveData<MovieModel>()
    private var movieCrew = MutableLiveData<MovieCrewModel>()
    private var state: SingleLiveEvent<MovieState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllMovies() {
        // Set State Loading = true
        state.value = MovieState.IsLoading(true)

        // fetchAllMovie from API
        api.fetchAllMovies(Constants.DEFAULT_PAGE, BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<WrappedListResponses<MovieModel>> {
                override fun onFailure(call: Call<WrappedListResponses<MovieModel>>, t: Throwable) {
                    // Set State Error -> message
                    state.value = MovieState.Error("Cannot connect to the server")
                    Log.d(Constants.TAG_ERROR, t.message!!)
                }

                override fun onResponse(
                    call: Call<WrappedListResponses<MovieModel>>,
                    response: Response<WrappedListResponses<MovieModel>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val body = response.body() as WrappedListResponses<MovieModel>
                            val m = body.datas
                            movies.postValue(m)
                            // Success
                            state.value = MovieState.IsSuccess(true)
                            Log.d(Constants.TAG_DATA, movies.value.toString())
                        }
                    } else {
                        // Set State Error -> message
                        state.value = MovieState.Error("Failed to get results")
                        Log.d(Constants.TAG_ERROR, response.body().toString())
                    }
                    // Set State Loading = false
                    state.value = MovieState.IsLoading(false)
                }

            })
    }

    fun fetchMovieDetail(movie_id: Int) {
        // Set State Loading = true
        state.value = MovieState.IsLoading(true)

        // fetchDetailMovie from API
        api.getDetailMovie(movie_id, BuildConfig.TMDB_API_KEY).enqueue(object : Callback<MovieModel> {
            override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                // Set State Error -> message
                state.value = MovieState.Error("Cannot connect to the server")
                Log.d(Constants.TAG_ERROR, t.message!!)
            }

            override fun onResponse(call: Call<MovieModel>, response: Response<MovieModel>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body() as MovieModel
                        movie.postValue(body)
                        // Success
                        state.value = MovieState.IsSuccess(true)
                        Log.d(Constants.TAG_DATA, movie.value.toString())
                    }
                } else {
                    // Set State Error -> message
                    state.value = MovieState.Error("Failed to get results")
                    Log.d(Constants.TAG_ERROR, response.body().toString())
                }
                // Set State Loading = false
                state.value = MovieState.IsLoading(false)
            }

        })

    }

    fun fetchMovieCrew(movie_id: Int) {
        // Set State Loading = true
        state.value = MovieState.IsLoading(true)

        // fetchMovieCrew from API
        api.fetchMovieCredits(movie_id, BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<MovieCrewModel> {
                override fun onFailure(call: Call<MovieCrewModel>, t: Throwable) {
                    // Set State Error -> message
                    state.value = MovieState.Error("Cannot connect to the server")
                    Log.d(Constants.TAG_ERROR, t.message!!)
                }

                override fun onResponse(
                    call: Call<MovieCrewModel>,
                    response: Response<MovieCrewModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val body = response.body() as MovieCrewModel
                            movieCrew.postValue(body)
                            // Success
                            state.value = MovieState.IsSuccess(true)
                            Log.d(Constants.TAG_DATA, movieCrew.value.toString())
                        }

                    } else {
                        // Set State Error -> message
                        state.value = MovieState.Error("Failed to get results")
                        Log.d(Constants.TAG_ERROR, response.body().toString())
                    }
                    // Set State Loading = false
                    state.value = MovieState.IsLoading(false)
                }

            })
    }

    fun getMovies(): LiveData<List<MovieModel>> = movies
    fun getMovieDetail(): LiveData<MovieModel> = movie
    fun getMovieCredits(): LiveData<MovieCrewModel> = movieCrew
    fun getMovieState() = state
}

/*
    Sealed classes are used for representing restricted class hierarchies,
    when a value can have one of the types from a limited set,
    but cannot have any other type.
    and always extends on base class
 */
sealed class MovieState {
    data class IsLoading(var state: Boolean = false) : MovieState()
    data class Error(var error: String?) : MovieState()
    data class IsSuccess(var success: Boolean = false) : MovieState()
}