package com.ilham.made.consumerfavorite.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    @SerializedName("status_message")
    val status_message: String? = null,
    @SerializedName("status_code")
    val status_code: Int? = null,
    @SerializedName("results")
    val results: List<T>? = null
)