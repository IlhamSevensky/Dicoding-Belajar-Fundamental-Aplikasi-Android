package com.ilham.made.consumerfavorite.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ilham.made.consumerfavorite.data.db.movie.getMovieDatabase
import com.ilham.made.consumerfavorite.data.db.tvshow.getTvShowDatabase
import com.ilham.made.consumerfavorite.data.repository.MovieRepository
import com.ilham.made.consumerfavorite.data.repository.TvShowRepository
import com.ilham.made.consumerfavorite.models.MovieModel
import com.ilham.made.consumerfavorite.models.TvShowModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = SearchViewModel::class.java.simpleName
    }

    private val movieRepository =
        MovieRepository(
            getMovieDatabase(application)
        )

    private val tvShowRepository =
        TvShowRepository(
            getTvShowDatabase(application)
        )

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var movieResult = MutableLiveData<List<MovieModel>>()
    private var tvShowResult = MutableLiveData<List<TvShowModel>>()

    var searchMovieState = movieRepository.getMovieState()
    var searchTvShowState = tvShowRepository.getTvShowState()

    private fun getSearchResultMovie(query: String) {
        viewModelScope.launch {
            try {
                val data = movieRepository.resultMovieSearch(query)
                Log.d(TAG, "Movie Search Fetch Data : $data")
                movieResult.postValue(data)
                Log.d(TAG, "Movie Search Result : $movieResult")
            } catch (networkError: IOException) {
                Log.d(TAG, "Error Network ${networkError.message}")
            }
        }
    }

    private fun getSearchResultTvShow(query: String) {
        viewModelScope.launch {
            try {
                val data = tvShowRepository.resultTvShowSearch(query)
                Log.d(TAG, "TV Show Search Fetch Data :  $data")
                tvShowResult.postValue(data)
                Log.d(TAG, "TV Show Search Result : $tvShowResult")
            } catch (networkError: IOException) {
                Log.d(TAG, "Error Network ${networkError.message}")
            }
        }
    }

    fun getTvShowResult(query: String): LiveData<List<TvShowModel>> {
        getSearchResultTvShow(query)
        return tvShowResult
    }

    fun getMovieResult(query: String): LiveData<List<MovieModel>>? {
        getSearchResultMovie(query)
        return movieResult
    }

    fun recoverMovieResult(): LiveData<List<MovieModel>> = movieResult
    fun recoverTvShowResult(): LiveData<List<TvShowModel>> = tvShowResult

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "SearchViewModel Cleared")
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }

    }

}