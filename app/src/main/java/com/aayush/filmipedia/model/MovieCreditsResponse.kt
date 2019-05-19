package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose

data class MovieCreditsResponse(
    @Expose var id: Long? = null,
    @Expose var cast: List<MovieCast>? = null,
    @Expose var crew: List<MovieCrew>? = null
)
