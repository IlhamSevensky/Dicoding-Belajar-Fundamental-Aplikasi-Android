package com.ilham.made.fifthsubmission.data.db.favorite.tvshow

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilham.made.fifthsubmission.models.FavoriteTvShowModel

@Entity(tableName = "favorite_tvshow_database")
data class FavoriteTvShowDatabaseEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    val id: Int? = 0,
    val tvshow_id: String?,
    val title: String?,
    val overview: String?,
    val first_air_date: String?,
    val poster: String?,
    val backdrop: String?

)

fun List<FavoriteTvShowDatabaseEntity>.asFavoriteTvShowModel(): List<FavoriteTvShowModel> {
    return map {
        FavoriteTvShowModel(
            id = it.id!!,
            tvshow_id = it.tvshow_id,
            title = it.title,
            overview = it.overview,
            first_air_date = it.first_air_date,
            poster = it.poster,
            backdrop = it.backdrop
        )
    }
}