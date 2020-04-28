package com.ilham.made.consumerfavorite.models

data class FavoriteTvShowModel(
    val id: Int = 0,
    var tvshow_id: String? = null,
    var title: String? = null,
    var overview: String? = null,
    var first_air_date: String? = null,
    var poster: String? = null,
    var backdrop: String? = null
)