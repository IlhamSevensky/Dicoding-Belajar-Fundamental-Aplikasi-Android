package com.ilham.made.fifthsubmission.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ilham.made.fifthsubmission.data.db.movie.getMovieDatabase
import com.ilham.made.fifthsubmission.data.db.tvshow.getTvShowDatabase
import com.ilham.made.fifthsubmission.data.repository.MovieRepository
import com.ilham.made.fifthsubmission.data.repository.TvShowRepository
import com.ilham.made.fifthsubmission.models.MovieCrewModel
import com.ilham.made.fifthsubmission.models.MovieModel
import com.ilham.made.fifthsubmission.models.TvShowModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
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

    var movieState = movieRepository.getMovieState()
    var tvshowState = tvShowRepository.getTvShowState()

    private var movieDetail = MutableLiveData<MovieModel>()
    private var movieCrew = MutableLiveData<MovieCrewModel>()
    private var tvShowDetail = MutableLiveData<TvShowModel>()

    fun getMovieInformation(movieId: Int) {
        viewModelScope.launch {
            if (movieDetail.value == null){
                movieDetail.postValue(movieRepository.getMovieDetail(movieId))
            }
            if (movieDetail.value == null) {
                movieCrew.postValue(movieRepository.getMovieCrew(movieId))
            }

        }
    }

    fun getTvShowInformation(tvshowId: Int) {
        viewModelScope.launch {
            if (tvShowDetail.value == null){
                tvShowDetail.postValue(tvShowRepository.getTvShowInformation(tvshowId))
            }
        }
    }

    fun getMovieDetail(): LiveData<MovieModel>? = movieDetail
    fun getMovieCrew(): LiveData<MovieCrewModel>? = movieCrew
    fun getTvShowDetail(): LiveData<TvShowModel>? = tvShowDetail


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "MovieViewModel Cleared")
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }

    }

}