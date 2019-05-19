package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Crew
import com.aayush.filmipedia.util.datasource.CrewDataSource
import io.reactivex.disposables.CompositeDisposable

class CrewDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val personId: Long?
): DataSource.Factory<Long, Crew>() {
    val mutableLiveData: MutableLiveData<CrewDataSource> = MutableLiveData()
    private lateinit var crewDataSource: CrewDataSource

    override fun create(): DataSource<Long, Crew> {
        crewDataSource = CrewDataSource(application, compositeDisposable, personId)
        mutableLiveData.postValue(crewDataSource)
        return crewDataSource
    }
}