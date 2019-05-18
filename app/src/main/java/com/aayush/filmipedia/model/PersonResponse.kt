package com.aayush.filmipedia.model

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("page") var page: Int = 0,
    @SerializedName("results") var results: List<PersonResult>? = null,
    @SerializedName("total_results") var totalResults: Int = 0,
    @SerializedName("total_pages") var totalPages: Long? = null
)