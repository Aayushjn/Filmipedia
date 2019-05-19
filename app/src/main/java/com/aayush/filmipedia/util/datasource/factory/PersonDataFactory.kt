package com.aayush.filmipedia.util.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.PersonResult
import com.aayush.filmipedia.util.datasource.PersonDataSource
import io.reactivex.disposables.CompositeDisposable

class PersonDataFactory(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val query: String
): DataSource.Factory<Long, PersonResult>() {
    val mutableLiveData = MutableLiveData<PersonDataSource>()
    private lateinit var personDataSource: PersonDataSource

    override fun create(): DataSource<Long, PersonResult> {
        personDataSource = PersonDataSource(application, compositeDisposable, query)
        mutableLiveData.postValue(personDataSource)
        return personDataSource
    }
}