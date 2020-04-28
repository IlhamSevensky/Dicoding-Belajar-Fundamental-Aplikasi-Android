package com.ilham.made.fifthsubmission.utils

import android.provider.BaseColumns

class Constants {

    companion object {
        const val API_ENDPOINT = "https://api.themoviedb.org/3/"
        const val API_IMAGE_ENDPOINT = "https://image.tmdb.org/t/p/"

        const val ACTIVITY_TYPE_MOVIE = "movie"
        const val ACTIVITY_TYPE_TVSHOW = "tv_show"
        const val ENDPOINT_POSTER_SIZE_W185 = "w185"
        const val ENDPOINT_POSTER_SIZE_W780 = "w780"
        const val JOB_DIRECTOR = "Director"

        /**
         * Column name
         */
        const val FAVORITE_MOVIE_TABLE_NAME = "favorite_movie_database"
        const val FAVORITE_TVSHOW_TABLE_NAME = "favorite_tvshow_database"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TVSHOW_ID = "tvshow_id"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_RELEASE_DATE = "release_date"
        const val COLUMN_FIRST_AIR_DATE = "first_air_date"
        const val COLUMN_POSTER = "poster"
        const val COLUMN_BACKDROP = "backdrop"
    }

}