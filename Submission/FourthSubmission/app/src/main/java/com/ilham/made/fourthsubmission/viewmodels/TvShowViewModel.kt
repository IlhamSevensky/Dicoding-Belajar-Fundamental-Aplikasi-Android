package com.ilham.made.fourthsubmission.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilham.made.fourthsubmission.BuildConfig
import com.ilham.made.fourthsubmission.models.TvShowModel
import com.ilham.made.fourthsubmission.utils.Constants
import com.ilham.made.fourthsubmission.utils.SingleLiveEvent
import com.ilham.made.fourthsubmission.utils.WrappedListResponses
import com.ilham.made.fourthsubmission.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowViewModel : ViewModel() {
    /*
        MutableLiveData valuenya bisa diubah,
        LiveData valuenya tidak bisa diubah langsung
     */
    private var tvShows = MutableLiveData<List<TvShowModel>>()
    private var tvShow = MutableLiveData<TvShowModel>()
    private var state: SingleLiveEvent<TvShowState> = SingleLiveEvent()
    private val api = ApiClient.instance()

    fun fetchAllTvShow() {
        // Set State Loading = true
        state.value = TvShowState.IsLoading(true)

        // fetch Top Rated Tv Show from API
        api.fetchAllTvShow(Constants.DEFAULT_PAGE, BuildConfig.TMDB_API_KEY)
            .enqueue(object : Callback<WrappedListResponses<TvShowModel>> {
                override fun onFailure(
                    call: Call<WrappedListResponses<TvShowModel>>,
                    t: Throwable
                ) {
                    // Set State Error -> message
                    state.value = TvShowState.Error("Cannot connect to the server")
                    Log.d(Constants.TAG_ERROR, t.message!!)
                }

                override fun onResponse(
                    call: Call<WrappedListResponses<TvShowModel>>,
                    response: Response<WrappedListResponses<TvShowModel>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val body = response.body() as WrappedListResponses<TvShowModel>
                            val m = body.datas
                            tvShows.postValue(m)
                            // Success
                            state.value = TvShowState.IsSuccess(true)
                            Log.d(Constants.TAG_DATA, tvShows.value.toString())
                        }

                    } else {
                        // Set State Error -> message
                        state.value = TvShowState.Error("Failed to get results")
                        Log.d(Constants.TAG_ERROR, response.body().toString())
                    }
                    // Set State Loading = false
                    state.value = TvShowState.IsLoading(false)
                }

            })
    }

    fun fetchTvShowDetail(tv_id: Int) {
        // Set State Loading = true
        state.value = TvShowState.IsLoading(true)

        // fetch TV Show Detail from API
        api.getDetailTv(tv_id, BuildConfig.TMDB_API_KEY).enqueue(object : Callback<TvShowModel> {
            override fun onFailure(call: Call<TvShowModel>, t: Throwable) {
                // Set State Error -> message
                state.value = TvShowState.Error("Cannot connect to the server")
                Log.d(Constants.TAG_ERROR, t.message!!)
            }

            override fun onResponse(call: Call<TvShowModel>, response: Response<TvShowModel>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val m = response.body() as TvShowModel
                        tvShow.postValue(m)
                        // Success
                        state.value = TvShowState.IsSuccess(true)
                        Log.d(Constants.TAG_DATA, tvShow.value.toString())
                    }
                } else {
                    // Set State Error -> message
                    state.value = TvShowState.Error("Failed to get results")
                    Log.d(Constants.TAG_ERROR, response.body().toString())
                }
                // Set State Loading = false
                state.value = TvShowState.IsLoading(false)
            }

        })
    }

    fun getAllTvShow(): LiveData<List<TvShowModel>> = tvShows
    fun getOneTvShow(): LiveData<TvShowModel> = tvShow
    fun getTvShowState() = state
}

/*
    Sealed classes are used for representing restricted class hierarchies,
    when a value can have one of the types from a limited set,
    but cannot have any other type.
    and always extends on base class
 */
sealed class TvShowState {
    data class IsLoading(var state: Boolean = false) : TvShowState()
    data class Error(var error: String?) : TvShowState()
    data class IsSuccess(var success: Boolean = false) : TvShowState()
}