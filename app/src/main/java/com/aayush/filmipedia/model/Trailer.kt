package com.aayush.filmipedia.model

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("key") var key: String?,
    @SerializedName("name") var name: String?
)