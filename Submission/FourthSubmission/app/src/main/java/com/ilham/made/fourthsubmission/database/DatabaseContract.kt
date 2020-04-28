package com.ilham.made.fourthsubmission.database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns: BaseColumns {
        companion object {
            const val MOVIE_FAVORITE_TABLE_NAME = "movie_favorite"
            const val TVSHOW_FAVORITE_TABLE_NAME = "tvshow_favorite"
            const val MOVIE_ID = "movie_id"
            const val TVSHOW_ID = "tvshow_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val POSTER = "poster_path"
            const val BACKDROP = "backdrop_path"
        }
    }

}