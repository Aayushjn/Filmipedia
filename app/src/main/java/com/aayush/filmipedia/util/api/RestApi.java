package com.aayush.filmipedia.util.api;

import com.aayush.filmipedia.model.CreditsResponse;
import com.aayush.filmipedia.model.GenreResponse;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.model.MovieCreditsResponse;
import com.aayush.filmipedia.model.MovieResponse;
import com.aayush.filmipedia.model.Person;
import com.aayush.filmipedia.model.PersonResponse;
import com.aayush.filmipedia.model.TrailerResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {
    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey,
                                               @Query("page") Long page,
                                               @Query("region") String region);

    @GET("search/movie")
    Observable<MovieResponse> searchMovies(@Query("api_key") String apiKey,
                                           @Query("query") String query,
                                           @Query("page") Long page,
                                           @Query("region") String region);

    @GET("search/person")
    Observable<PersonResponse> searchPeople(@Query("api_key") String apiKey,
                                            @Query("query") String query,
                                            @Query("page") Long page,
                                            @Query("region") String region);

    @GET("genre/movie/list")
    Observable<GenreResponse> getMovieGenres(@Query("api_key") String apiKey);

    @GET("discover/movie")
    Observable<MovieResponse> getMoviesByGenre(@Query("api_key") String apiKey,
                                               @Query("page") Long page,
                                               @Query("with_genres") int genreId,
                                               @Query("region") String region);

    @GET("person/popular")
    Observable<PersonResponse> getPopularPeople(@Query("api_key") String apiKey,
                                                @Query("page") Long page);

    @GET("movie/{movie_id}")
    Observable<Movie> getMovieById(@Path("movie_id") Long id,
                                   @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Observable<MovieCreditsResponse> getMovieCredits(@Path("movie_id") Long id,
                                                     @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<TrailerResponse> getMovieTrailers(@Path("movie_id") Long id,
                                           @Query("api_key") String apiKey);

    @GET("person/{person_id}")
    Observable<Person> getPersonById(@Path("person_id") Long id,
                               @Query("api_key") String apiKey);

    @GET("person/{person_id}/movie_credits")
    Observable<CreditsResponse> getCreditsById(@Path("person_id") Long id,
                                               @Query("api_key") String apiKey);
}
