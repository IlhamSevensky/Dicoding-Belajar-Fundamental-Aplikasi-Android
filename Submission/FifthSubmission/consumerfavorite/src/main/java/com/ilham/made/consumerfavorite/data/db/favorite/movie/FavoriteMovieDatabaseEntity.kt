package com.ilham.made.consumerfavorite.data.db.favorite.movie

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilham.made.consumerfavorite.models.FavoriteMovieModel

@Entity(tableName = "favorite_movie_database")
data class FavoriteMovieDatabaseEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    val id: Int? = 0,
    val movie_id: String?,
    val title: String?,
    val overview: String?,
    val release_date: String?,
    val poster: String?,
    val backdrop: String?

)

fun List<FavoriteMovieDatabaseEntity>.asFavoriteMovieModel(): List<FavoriteMovieModel> {
    return map {
        FavoriteMovieModel(
            id = it.id,
            movie_id = it.movie_id,
            title = it.title,
            overview = it.overview,
            release_date = it.release_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}