package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Cast
import com.aayush.filmipedia.util.datasource.CastDataSource
import io.reactivex.disposables.CompositeDisposable

class CastDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val personId: Long?
): DataSource.Factory<Long, Cast>() {
    val mutableLiveData = MutableLiveData<CastDataSource>()
    private lateinit var castDataSource: CastDataSource

    override fun create(): DataSource<Long, Cast> {
        castDataSource = CastDataSource(application, compositeDisposable, personId).apply {  }
        mutableLiveData.postValue(castDataSource)
        return castDataSource
    }
}