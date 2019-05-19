package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.datasource.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
): DataSource.Factory<Long, Movie>() {
    val mutableLiveData = MutableLiveData<MovieDataSource>()
    private lateinit var movieDataSource: MovieDataSource

    override fun create(): DataSource<Long, Movie> {
        movieDataSource = MovieDataSource(application, compositeDisposable, type)
        mutableLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}