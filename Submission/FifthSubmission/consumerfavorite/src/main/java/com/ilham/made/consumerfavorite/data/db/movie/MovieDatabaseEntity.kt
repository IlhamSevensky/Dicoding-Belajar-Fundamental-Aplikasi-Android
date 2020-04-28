package com.ilham.made.consumerfavorite.data.db.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilham.made.consumerfavorite.models.MovieModel

@Entity(tableName = "movie_database")
data class MovieDatabaseEntity constructor(

    @PrimaryKey
    val movie_id: Int,
    val title: String?,
    val overview: String?,
    val release_date: String?,
    val poster: String?,
    val backdrop: String?

)

fun List<MovieDatabaseEntity>.asMovieModel(): List<MovieModel> {
    return map {
        MovieModel(
            movie_id = it.movie_id,
            title = it.title,
            overview = it.overview,
            release_date = it.release_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}