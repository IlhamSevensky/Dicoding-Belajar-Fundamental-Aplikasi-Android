package com.ilham.made.fifthsubmission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ilham.made.fifthsubmission.BuildConfig
import com.ilham.made.fifthsubmission.data.db.tvshow.TvShowDatabase
import com.ilham.made.fifthsubmission.data.db.tvshow.asTvShowModel
import com.ilham.made.fifthsubmission.models.TvShowModel
import com.ilham.made.fifthsubmission.models.asTvShowDatabaseEntity
import com.ilham.made.fifthsubmission.utils.SingleLiveEvent
import com.ilham.made.fifthsubmission.utils.WrappedListResponse
import com.ilham.made.fifthsubmission.webservices.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class TvShowRepository(private val database: TvShowDatabase) {

    companion object {
        private val TAG = TvShowRepository::class.java.simpleName
    }

    val tvshows: LiveData<List<TvShowModel>> = Transformations.map(database.tvshowDao.getTvShow()) {
        it.asTvShowModel()
    }

    private var state: SingleLiveEvent<TvShowState> = SingleLiveEvent()

    suspend fun resultTvShowSearch(query: String): List<TvShowModel>? {
        state.value = TvShowState.IsLoading(true)
        try {
            val response = ApiClient.instance.searchTvShow(BuildConfig.TMDB_API_KEY, query).awaitResponse()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    state.value = TvShowState.IsLoading(false)
                    return response.body()!!.results
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ERROR : ${e.message}")
            state.postValue(TvShowState.Error("Cannot connect to the server"))
        }

        return null
    }

    suspend fun getTvShowInformation(tvshowId: Int) : TvShowModel? {
        state.value = TvShowState.IsLoading(true)
        try {
            val response = ApiClient.instance.getTvShowInformation(tvshowId).awaitResponse()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    state.value = TvShowState.IsLoading(false)
                    return response.body()
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ERROR : ${e.message}")
            state.postValue(TvShowState.Error("Cannot connect to the server"))
        }

        return null
    }

    fun refreshTvShow() {
        state.value = TvShowState.IsLoading(true)
        ApiClient.instance.getTopRatedTvShow().enqueue(object :
            Callback<WrappedListResponse<TvShowModel>> {
            override fun onFailure(call: Call<WrappedListResponse<TvShowModel>>, t: Throwable) {
                state.value = TvShowState.Error("Cannot connect to the server")
            }

            override fun onResponse(
                call: Call<WrappedListResponse<TvShowModel>>,
                response: Response<WrappedListResponse<TvShowModel>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body()!!.results?.asTvShowDatabaseEntity()
                        if (body != null) {
                            GlobalScope.launch(Dispatchers.IO) {
                                database.tvshowDao.insertAllTvShow(body)
                            }
                            state.value = TvShowState.IsSuccess(true)
                        }
                    }
                } else {
                    state.value = TvShowState.Error("Failed to get results")
                }

                state.value = TvShowState.IsLoading(false)
            }

        })
    }

    fun getTvShowState() = state
}

sealed class TvShowState {
    data class IsLoading(var state: Boolean = false) : TvShowState()
    data class Error(var error: String?) : TvShowState()
    data class IsSuccess(var success: Boolean = false) : TvShowState()
}

