package com.ilham.made.consumerfavorite.ui.favorite.tvshow

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilham.made.consumerfavorite.data.db.favorite.tvshow.getFavoriteTvShowDatabase
import com.ilham.made.consumerfavorite.data.repository.FavoriteTvShowRepository

class FavoriteTvShowViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = FavoriteTvShowViewModel::class.java.simpleName
    }

    private val favoriteTvShowRepository =
        FavoriteTvShowRepository(
            getFavoriteTvShowDatabase(application)
        )

    val favoriteTvShows = favoriteTvShowRepository.favTvShow

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "MovieViewModel Cleared")
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteTvShowViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteTvShowViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }

    }

}