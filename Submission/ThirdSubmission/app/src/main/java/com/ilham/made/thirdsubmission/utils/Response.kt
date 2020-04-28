package com.ilham.made.thirdsubmission.utils

import com.google.gson.annotations.SerializedName

// Untuk mengambil respon utama pada json ( SerializedName harus sama pada key di JSON yang paling luar/root )
// Response untuk List Movie dan TV Show
data class WrappedListResponses<T>(
    @SerializedName("status_message") var status_message: String? = null,
    @SerializedName("status_code") var status_code: Int? = null,
    @SerializedName("results") var datas: List<T>? = null
)