package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCrew
import com.aayush.filmipedia.util.datasource.MovieCrewDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieCrewDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val movieId: Long?
): DataSource.Factory<Long, MovieCrew>() {
    val mutableLiveData: MutableLiveData<MovieCrewDataSource> = MutableLiveData()
    private lateinit var movieCrewDataSource: MovieCrewDataSource

    override fun create(): DataSource<Long, MovieCrew> {
        movieCrewDataSource = MovieCrewDataSource(application, compositeDisposable, movieId)
        mutableLiveData.postValue(movieCrewDataSource)
        return movieCrewDataSource
    }
}