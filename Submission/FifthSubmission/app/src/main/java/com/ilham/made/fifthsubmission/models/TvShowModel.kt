package com.ilham.made.fifthsubmission.models

import com.google.gson.annotations.SerializedName
import com.ilham.made.fifthsubmission.data.db.tvshow.TvShowDatabaseEntity

data class TvShowModel(
    @SerializedName("id")
    val tv_show_id: Int = 0,
    @SerializedName("name")
    val title: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("first_air_date")
    val first_air_date: String? = null,
    @SerializedName("poster_path")
    val poster: String? = null,
    @SerializedName("backdrop_path")
    val backdrop: String? = null,
    @SerializedName("created_by")
    val created_by: ArrayList<TvShowCreatorModel>? = null,
    @SerializedName("genres")
    val genre: ArrayList<TvShowGenreModel>? = null,
    @SerializedName("episode_run_time")
    val runtime: ArrayList<Int>? = null
)

data class TvShowCreatorModel(
    @SerializedName("name")
    val name: String? = null
)

data class TvShowGenreModel(
    @SerializedName("name")
    val name: String? = null
)

fun List<TvShowModel>.asTvShowDatabaseEntity(): List<TvShowDatabaseEntity> {
    return map {
        TvShowDatabaseEntity(
            tvshow_id = it.tv_show_id,
            title = it.title,
            overview = it.overview,
            first_air_date = it.first_air_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}