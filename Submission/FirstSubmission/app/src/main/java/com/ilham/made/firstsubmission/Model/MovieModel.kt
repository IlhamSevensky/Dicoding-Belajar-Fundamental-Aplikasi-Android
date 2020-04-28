package com.ilham.made.firstsubmission.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel (
    var movie_name: String,
    var movie_desc: String,
    var movie_image: Int,
    var movie_director: String,
    var movie_runtime: String,
    var movie_genre: String,
    var movie_image_highlight: Int
) : Parcelable