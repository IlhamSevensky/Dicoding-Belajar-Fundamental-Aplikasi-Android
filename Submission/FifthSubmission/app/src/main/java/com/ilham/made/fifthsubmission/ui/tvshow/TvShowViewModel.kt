package com.ilham.made.fifthsubmission.ui.tvshow

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilham.made.fifthsubmission.data.db.tvshow.getTvShowDatabase
import com.ilham.made.fifthsubmission.data.repository.TvShowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class TvShowViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = TvShowViewModel::class.java.simpleName
    }

    private val tvShowRepository =
        TvShowRepository(
            getTvShowDatabase(application)
        )

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val tvShows = tvShowRepository.tvshows

    var state = tvShowRepository.getTvShowState()

    init {
        refreshTvShowFromRepository()
    }

    fun refreshTvShowFromRepository() {
        viewModelScope.launch {
            try {
                tvShowRepository.refreshTvShow()
            } catch (networkError: IOException) {
                Log.d(TAG, "Error Network ${networkError.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "TvShowViewModel Cleared")
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TvShowViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TvShowViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }

    }

}