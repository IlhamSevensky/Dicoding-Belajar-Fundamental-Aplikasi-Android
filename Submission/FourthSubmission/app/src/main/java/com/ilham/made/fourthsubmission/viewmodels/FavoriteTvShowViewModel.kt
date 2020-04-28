package com.ilham.made.fourthsubmission.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilham.made.fourthsubmission.database.TvShowFavoriteHelper
import com.ilham.made.fourthsubmission.helper.MappingHelper
import com.ilham.made.fourthsubmission.models.FavoriteTvShowModel
import com.ilham.made.fourthsubmission.utils.SingleLiveEvent

class FavoriteTvShowViewModel : ViewModel() {

    private var tvShowsFav = MutableLiveData<List<FavoriteTvShowModel>>()
    private var state: SingleLiveEvent<FavoriteTvShowState> = SingleLiveEvent()

    fun fetchAllTvShowFav(context: Context) {
        // Set State Loading = true
        state.value = FavoriteTvShowState.IsLoading(true)

        val tvShowFavoriteHelper = TvShowFavoriteHelper(context)
        tvShowFavoriteHelper.open()
        val cursor = tvShowFavoriteHelper.queryAll()
        val data = MappingHelper.mapTvShowCursorToArrayList(cursor)

        if (data.isNotEmpty()) {
            tvShowsFav.postValue(data as List<FavoriteTvShowModel>)
            state.value = FavoriteTvShowState.IsSuccess(true)
        } else {
            state.value = FavoriteTvShowState.IsEmpty(true)
        }

        tvShowFavoriteHelper.close()
    }

    fun getTvShowFav(): LiveData<List<FavoriteTvShowModel>> = tvShowsFav
    fun getTvShowFavState() = state
}

/*
    Sealed classes are used for representing restricted class hierarchies,
    when a value can have one of the types from a limited set,
    but cannot have any other type.
    and always extends on base class
 */
sealed class FavoriteTvShowState {
    data class IsLoading(var state: Boolean = false) : FavoriteTvShowState()
    data class IsEmpty(var empty: Boolean = false) : FavoriteTvShowState()
    data class IsSuccess(var success: Boolean = false) : FavoriteTvShowState()
}