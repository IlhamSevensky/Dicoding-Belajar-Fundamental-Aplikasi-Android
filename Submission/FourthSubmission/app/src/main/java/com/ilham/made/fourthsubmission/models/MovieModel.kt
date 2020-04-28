package com.ilham.made.fourthsubmission.models

import com.google.gson.annotations.SerializedName

// Model dengan SerializeName yang merujuk pada key results (isi/POJO dari key results di JSON) dari response.kt
data class MovieModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("poster_path") var poster: String? = null,
    @SerializedName("backdrop_path") var backdrop: String? = null,
    @SerializedName("genres") var genre: ArrayList<MovieGenreModel>? = null,
    @SerializedName("runtime") var duration: Int? = null
)

data class MovieGenreModel(
    @SerializedName("name") var name: String? = null
)

data class MovieCrewModel(
    @SerializedName("crew") var director: ArrayList<MovieCrewDetailModel>? = null
)

data class MovieCrewDetailModel(
    @SerializedName("job") var job: String? = null,
    @SerializedName("name") var name: String? = null
)