package com.ilham.made.consumerfavorite.data.db.tvshow

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilham.made.consumerfavorite.models.TvShowModel

@Entity(tableName = "tvshow_database")
data class TvShowDatabaseEntity constructor(

    @PrimaryKey
    val tvshow_id: Int,
    val title: String?,
    val overview: String?,
    val first_air_date: String?,
    val poster: String?,
    val backdrop: String?

)

fun List<TvShowDatabaseEntity>.asTvShowModel(): List<TvShowModel> {
    return map {
        TvShowModel(
            tv_show_id = it.tvshow_id,
            title = it.title,
            overview = it.overview,
            first_air_date = it.first_air_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}