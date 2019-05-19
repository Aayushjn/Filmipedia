package com.aayush.filmipedia.model

import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    @SerializedName("id") var trailerId: Int = 0,
    @SerializedName("results") var results: List<Trailer>? = null
)