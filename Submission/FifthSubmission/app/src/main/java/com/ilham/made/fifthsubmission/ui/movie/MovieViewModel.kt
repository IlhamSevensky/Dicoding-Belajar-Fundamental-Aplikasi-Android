package com.ilham.made.fifthsubmission.ui.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilham.made.fifthsubmission.data.db.movie.getMovieDatabase
import com.ilham.made.fifthsubmission.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = MovieViewModel::class.java.simpleName
    }

    private val movieRepository =
        MovieRepository(
            getMovieDatabase(application)
        )

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val movies = movieRepository.movies

    var state = movieRepository.getMovieState()

    init {
        refreshMovieFromRepository()
    }

    fun refreshMovieFromRepository() {
        viewModelScope.launch {
            try {
                movieRepository.refreshMovies()
            } catch (networkError: IOException) {
                Log.d(TAG, "Error Network ${networkError.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "MovieViewModel Cleared")
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }

    }

}