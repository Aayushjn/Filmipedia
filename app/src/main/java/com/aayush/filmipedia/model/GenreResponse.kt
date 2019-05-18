package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @Expose var page: Long? = null,
    @Expose var genres: List<Genre>? = null,
    @SerializedName("total_pages") var totalPages: Long? = null,
    @SerializedName("total_results") var totalResults: Long? = null
)