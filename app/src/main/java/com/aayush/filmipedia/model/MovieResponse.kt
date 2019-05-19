package com.aayush.filmipedia.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page") var page: Long? = null,
    @SerializedName("results") var results: List<Movie>? = null,
    @SerializedName("total_results") var totalResults: Int = 0,
    @SerializedName("total_pages") var totalPages: Long? = null
)