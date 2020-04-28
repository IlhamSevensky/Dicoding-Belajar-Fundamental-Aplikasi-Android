package com.ilham.made.fifthsubmission.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.data.db.favorite.movie.FavoriteMovieDatabaseEntity
import com.ilham.made.fifthsubmission.data.db.favorite.movie.asFavoriteMovieModel
import com.ilham.made.fifthsubmission.models.FavoriteMovieModel
import com.ilham.made.fifthsubmission.widget.FavoriteMovieWidget

object Helper {

    fun setImage(context: Context, view: ImageView, imagePath: String) {
        Glide.with(context)
            .load(imagePath)
            .into(view)
    }

    fun updateFavoriteMovieWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, FavoriteMovieWidget::class.java)
        )
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
    }

    fun setImageNotAvailable(imageView: ImageView) {
        imageView.setImageResource(R.drawable.ic_no_image_available)
        imageView.setBackgroundColor(Color.GRAY)
    }

    fun isImagePathAvailable(imagePath: String?): Boolean {
        return imagePath != null
    }

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteMovieModel> {
        val movieFavoriteList = ArrayList<FavoriteMovieDatabaseEntity>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Constants.COLUMN_ID))
                val movieId = getString(getColumnIndexOrThrow(Constants.COLUMN_MOVIE_ID))
                val title = getString(getColumnIndexOrThrow(Constants.COLUMN_TITLE))
                val overview = getString(getColumnIndexOrThrow(Constants.COLUMN_OVERVIEW))
                val releaseDate = getString(getColumnIndexOrThrow(Constants.COLUMN_RELEASE_DATE))
                val poster = getString(getColumnIndexOrThrow(Constants.COLUMN_POSTER))
                val backdrop = getString(getColumnIndexOrThrow(Constants.COLUMN_BACKDROP))
                movieFavoriteList.add(
                    FavoriteMovieDatabaseEntity(
                        id,
                        movieId,
                        title,
                        overview,
                        releaseDate,
                        poster,
                        backdrop
                    )
                )
            }
        }
        return movieFavoriteList.asFavoriteMovieModel() as ArrayList<FavoriteMovieModel>
    }

}