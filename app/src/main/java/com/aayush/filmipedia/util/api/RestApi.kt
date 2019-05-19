package com.aayush.filmipedia.util.api

import com.aayush.filmipedia.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Long?,
        @Query("region") region: String
    ): Observable<MovieResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Long?,
        @Query("region") region: String
    ): Observable<MovieResponse>

    @GET("search/person")
    fun searchPeople(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Long?,
        @Query("region") region: String
    ): Observable<PersonResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(@Query("api_key") apiKey: String): Observable<GenreResponse>

    @GET("discover/movie")
    fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("page") page: Long?,
        @Query("with_genres") genreId: Int,
        @Query("region") region: String
    ): Observable<MovieResponse>

    @GET("person/popular")
    fun getPopularPeople(
        @Query("api_key") apiKey: String,
        @Query("page") page: Long?
    ): Observable<PersonResponse>

    @GET("movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") id: Long?,
        @Query("api_key") apiKey: String
    ): Observable<Movie>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") id: Long?,
        @Query("api_key") apiKey: String
    ): Observable<MovieCreditsResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailers(
        @Path("movie_id") id: Long?,
        @Query("api_key") apiKey: String
    ): Observable<TrailerResponse>

    @GET("person/{person_id}")
    fun getPersonById(
        @Path("person_id") id: Long?,
        @Query("api_key") apiKey: String
    ): Observable<Person>

    @GET("person/{person_id}/movie_credits")
    fun getCreditsById(
        @Path("person_id") id: Long?,
        @Query("api_key") apiKey: String
    ): Observable<CreditsResponse>
}