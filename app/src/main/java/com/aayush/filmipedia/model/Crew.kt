package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Crew(
    @Expose var adult: Boolean? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("credit_id") var creditId: String? = null,
    @Expose var department: String? = null,
    @SerializedName("genre_ids") var genreIds: List<Long>? = null,
    @Expose var id: Long? = null,
    @Expose var job: String? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @Expose var overview: String? = null,
    @Expose var popularity: Double? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @Expose var title: String? = null,
    @Expose var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Long? = null
)