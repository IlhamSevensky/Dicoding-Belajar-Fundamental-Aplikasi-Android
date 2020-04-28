package com.ilham.made.secondsubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelData(
    var name: String,
    var desc: String,
    var poster: Int,
    var img_preview: Int,
    var director: String,
    var duration: String,
    var genre: String
) : Parcelable