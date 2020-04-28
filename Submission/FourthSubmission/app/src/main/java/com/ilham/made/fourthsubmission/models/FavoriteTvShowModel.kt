package com.ilham.made.fourthsubmission.models

data class FavoriteTvShowModel(
    var id: Int = 0,
    var tvshow_id: String? = null,
    var title: String? = null,
    var overview: String? = null,
    var poster: String? = null,
    var backdrop: String? = null
)