package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Trailer
import com.aayush.filmipedia.util.datasource.TrailerDataSource
import io.reactivex.disposables.CompositeDisposable

class TrailerDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val movieId: Long?
): DataSource.Factory<Long, Trailer>() {
    val mutableLiveData = MutableLiveData<TrailerDataSource>()
    private lateinit var trailerDataSource: TrailerDataSource

    override fun create(): DataSource<Long, Trailer> {
        trailerDataSource = TrailerDataSource(application, compositeDisposable, movieId)
        mutableLiveData.postValue(trailerDataSource)
        return trailerDataSource
    }
}