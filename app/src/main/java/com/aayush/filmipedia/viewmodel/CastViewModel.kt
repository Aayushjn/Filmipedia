package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Cast
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.CastDataFactory
import io.reactivex.disposables.CompositeDisposable

class CastViewModel(application: FilmipediaApplication, personId: Long?): ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var castLiveData: LiveData<PagedList<Cast>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val castDataFactory = CastDataFactory(application, compositeDisposable, personId)
        networkState = Transformations.switchMap(castDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        castLiveData = LivePagedListBuilder(castDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}