package com.ilham.made.fourthsubmission.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.BACKDROP
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.OVERVIEW
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.POSTER
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TVSHOW_ID
import com.ilham.made.fourthsubmission.models.FavoriteMovieModel
import com.ilham.made.fourthsubmission.models.FavoriteTvShowModel


object MappingHelper {

    fun mapMovieCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteMovieModel> {
        val movieFavList = ArrayList<FavoriteMovieModel>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val movie_id = getString(getColumnIndexOrThrow(MOVIE_ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val overview = getString(getColumnIndexOrThrow(OVERVIEW))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                val backdrop = getString(getColumnIndexOrThrow(BACKDROP))
                movieFavList.add(FavoriteMovieModel(id, movie_id, title, overview, poster, backdrop))
            }
        }
        return movieFavList
    }


    fun mapTvShowCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteTvShowModel> {
        val tvShowFavList = ArrayList<FavoriteTvShowModel>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val tvshow_id = getString(getColumnIndexOrThrow(TVSHOW_ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val overview = getString(getColumnIndexOrThrow(OVERVIEW))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                val backdrop = getString(getColumnIndexOrThrow(BACKDROP))
                tvShowFavList.add(FavoriteTvShowModel(id, tvshow_id, title, overview, poster, backdrop))
            }
        }
        return tvShowFavList
    }
}