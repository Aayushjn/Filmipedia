package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Genre
import com.aayush.filmipedia.util.datasource.GenreDataSource
import io.reactivex.disposables.CompositeDisposable

class GenreDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Long, Genre>() {
    val mutableLiveData = MutableLiveData<GenreDataSource>()
    private lateinit var genreDataSource: GenreDataSource

    override fun create(): DataSource<Long, Genre> {
        genreDataSource = GenreDataSource(application, compositeDisposable)
        mutableLiveData.postValue(genreDataSource)
        return genreDataSource
    }
}