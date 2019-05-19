package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Crew
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.CrewDataFactory
import io.reactivex.disposables.CompositeDisposable

class CrewViewModel(application: FilmipediaApplication, personId: Long?): ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var crewLiveData: LiveData<PagedList<Crew>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val crewDataFactory = CrewDataFactory(application, compositeDisposable, personId)
        networkState = Transformations.switchMap(crewDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        crewLiveData = LivePagedListBuilder(crewDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}