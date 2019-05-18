package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieCrew(
    @SerializedName("credit_id") var creditId: String? = null,
    @Expose var department: String? = null,
    @Expose var gender: Long? = null,
    @Expose var id: Long? = null,
    @Expose var job: String? = null,
    @Expose var name: String? = null,
    @SerializedName("profile_path") var profilePath: String? = null
)