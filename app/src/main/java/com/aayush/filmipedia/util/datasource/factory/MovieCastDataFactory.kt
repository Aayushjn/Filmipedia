package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCast
import com.aayush.filmipedia.util.datasource.MovieCastDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieCastDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val movieId: Long?
): DataSource.Factory<Long, MovieCast>() {
    val mutableLiveData = MutableLiveData<MovieCastDataSource>()
    private lateinit var movieCastDataSource: MovieCastDataSource

    override fun create(): DataSource<Long, MovieCast> {
        movieCastDataSource = MovieCastDataSource(application, compositeDisposable, movieId)
        mutableLiveData.postValue(movieCastDataSource)
        return movieCastDataSource
    }
}