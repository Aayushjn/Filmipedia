package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.datasource.MovieResultDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieResultDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable, private val type: String
) : DataSource.Factory<Long, Movie>() {
    val mutableLiveData = MutableLiveData<MovieResultDataSource>()
    private lateinit var movieResultDataSource: MovieResultDataSource

    override fun create(): DataSource<Long, Movie> {
        movieResultDataSource = MovieResultDataSource(application, compositeDisposable, type)
        mutableLiveData.postValue(movieResultDataSource)
        return movieResultDataSource
    }
}