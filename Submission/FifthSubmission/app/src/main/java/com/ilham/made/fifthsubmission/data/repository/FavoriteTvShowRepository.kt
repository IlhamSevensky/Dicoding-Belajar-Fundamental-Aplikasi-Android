package com.ilham.made.fifthsubmission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ilham.made.fifthsubmission.data.db.favorite.tvshow.FavoriteTvShowDatabase
import com.ilham.made.fifthsubmission.data.db.favorite.tvshow.FavoriteTvShowDatabaseEntity
import com.ilham.made.fifthsubmission.data.db.favorite.tvshow.asFavoriteTvShowModel
import com.ilham.made.fifthsubmission.models.FavoriteTvShowModel
import com.ilham.made.fifthsubmission.models.TvShowModel

class FavoriteTvShowRepository(private val database: FavoriteTvShowDatabase) {

    companion object {
        private val TAG = FavoriteTvShowRepository::class.java.simpleName
    }

    val favTvShow: LiveData<List<FavoriteTvShowModel>>? =
        Transformations.map(database.favTvShowDao.getFavoriteTvShow()) {
            it.asFavoriteTvShowModel()
        }

    fun checkFavorite(tvshowId: String): Int {
        return database.favTvShowDao.checkFavoriteTvShow(tvshowId)
    }

    fun removeFavorite(tvshowId: String) {
        database.favTvShowDao.removeFavoriteTvShow(tvshowId)
        Log.d(TAG, "Successfully removed tvshow with id : $tvshowId from favorite")
        Transformations.map(database.favTvShowDao.getFavoriteTvShow()) {
            it.asFavoriteTvShowModel()
        }
    }

    fun addFavoriteTvShow(tvshow: TvShowModel) {
        val favTvShow = FavoriteTvShowModel()
        favTvShow.tvshow_id = tvshow.tv_show_id.toString()
        favTvShow.title = tvshow.title
        favTvShow.overview = tvshow.overview
        favTvShow.first_air_date = tvshow.first_air_date
        favTvShow.poster = tvshow.poster
        favTvShow.backdrop = tvshow.backdrop


        database.favTvShowDao.insertFavoriteTvShow(
            FavoriteTvShowDatabaseEntity(
                null,
                tvshow.tv_show_id.toString(),
                tvshow.title,
                tvshow.overview,
                tvshow.first_air_date,
                tvshow.poster,
                tvshow.backdrop
            )
        )
        Log.d(TAG, "Successfully added tvshow ${tvshow.title} to favorite")
    }

}