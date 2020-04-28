package com.ilham.made.consumerfavorite.models

data class FavoriteMovieModel(
    val id: Int? = null,
    var movie_id: String? = null,
    var title: String? = null,
    var overview: String? = null,
    var release_date: String? = null,
    var poster: String? = null,
    var backdrop: String? = null
)