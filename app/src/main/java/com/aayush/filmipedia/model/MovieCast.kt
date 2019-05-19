package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieCast(
    @SerializedName("cast_id") var castId: Long? = null,
    @Expose var character: String? = null,
    @SerializedName("credit_id") var creditId: String? = null,
    @Expose var gender: Long? = null,
    @Expose var id: Long? = null,
    @Expose var name: String? = null,
    @Expose var order: Long? = null,
    @SerializedName("profile_path") var profilePath: String? = null
)