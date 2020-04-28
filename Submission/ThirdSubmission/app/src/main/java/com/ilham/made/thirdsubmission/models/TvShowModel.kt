package com.ilham.made.thirdsubmission.models

import com.google.gson.annotations.SerializedName

data class TvShowModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var title: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("poster_path") var poster: String? = null,
    @SerializedName("backdrop_path") var backdrop: String? = null,
    @SerializedName("created_by") var created_by: ArrayList<TvShowCreatorModel>? = null,
    @SerializedName("genres") var genre: ArrayList<TvShowGenreModel>? = null,
    @SerializedName("episode_run_time") var runtime: ArrayList<Int>? = null
)

data class TvShowCreatorModel(
    @SerializedName("name") var name: String? = null
)

data class TvShowGenreModel(
    @SerializedName("name") var name: String? = null
)