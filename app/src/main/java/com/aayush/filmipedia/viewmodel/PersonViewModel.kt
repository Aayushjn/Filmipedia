package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.PersonResult
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.PersonDataFactory
import io.reactivex.disposables.CompositeDisposable

class PersonViewModel(application: FilmipediaApplication, query: String) : ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var personLiveData: LiveData<PagedList<PersonResult>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val personDataFactory = PersonDataFactory(application, compositeDisposable, query)
        networkState = Transformations.switchMap(personDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        personLiveData = LivePagedListBuilder(personDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}