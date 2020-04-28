package com.ilham.made.consumerfavorite.models

import com.google.gson.annotations.SerializedName
import com.ilham.made.consumerfavorite.data.db.movie.MovieDatabaseEntity

data class MovieModel(
    @SerializedName("id")
    val movie_id: Int = 0,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("release_date")
    val release_date: String? = null,
    @SerializedName("poster_path")
    val poster: String? = null,
    @SerializedName("backdrop_path")
    val backdrop: String? = null,
    @SerializedName("runtime")
    val runtime: Int? = null,
    @SerializedName("genres")
    val genres: ArrayList<MovieGenreModel>? = null
)

data class MovieGenreModel(
    @SerializedName("name")
    val name: String? = null
)

data class MovieCrewModel(
    @SerializedName("crew")
    val director: ArrayList<MovieCrewDetailModel>? = null
)

data class MovieCrewDetailModel(
    @SerializedName("job")
    val job: String? = null,
    @SerializedName("name")
    val name: String? = null
)


fun List<MovieModel>.asMovieDatabaseEntity(): List<MovieDatabaseEntity> {
    return map {
        MovieDatabaseEntity(
            movie_id = it.movie_id,
            title = it.title,
            overview = it.overview,
            release_date = it.release_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}