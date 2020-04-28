package com.ilham.made.fourthsubmission.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilham.made.fourthsubmission.database.MovieFavoriteHelper
import com.ilham.made.fourthsubmission.helper.MappingHelper
import com.ilham.made.fourthsubmission.models.FavoriteMovieModel
import com.ilham.made.fourthsubmission.utils.SingleLiveEvent

class FavoriteMovieViewModel : ViewModel() {

    private var moviesFav = MutableLiveData<List<FavoriteMovieModel>>()
    private var state: SingleLiveEvent<FavoriteMovieState> = SingleLiveEvent()

    fun fetchAllMovieFav(context: Context) {
        // Set State Loading = true
        state.value = FavoriteMovieState.IsLoading(true)

        val movieFavoriteHelper = MovieFavoriteHelper(context)
        movieFavoriteHelper.open()
        val cursor = movieFavoriteHelper.queryAll()
        val data = MappingHelper.mapMovieCursorToArrayList(cursor)

        if (data.isNotEmpty()) {
            moviesFav.postValue(data as List<FavoriteMovieModel>)
            state.value = FavoriteMovieState.IsSuccess(true)
        } else {
            state.value = FavoriteMovieState.IsEmpty(true)
        }
        movieFavoriteHelper.close()
    }

    fun getMoviesFav(): LiveData<List<FavoriteMovieModel>> = moviesFav
    fun getMovieFavState() = state
}

/*
    Sealed classes are used for representing restricted class hierarchies,
    when a value can have one of the types from a limited set,
    but cannot have any other type.
    and always extends on base class
 */
sealed class FavoriteMovieState {
    data class IsLoading(var state: Boolean = false) : FavoriteMovieState()
    data class IsEmpty(var empty: Boolean = false) : FavoriteMovieState()
    data class IsSuccess(var success: Boolean = false) : FavoriteMovieState()
}