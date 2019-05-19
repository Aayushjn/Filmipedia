package com.aayush.filmipedia.model

import com.google.gson.annotations.Expose

data class CreditsResponse(
    @Expose var cast: List<Cast>? = null,
    @Expose var crew: List<Crew>? = null,
    @Expose var id: Long? = null
)