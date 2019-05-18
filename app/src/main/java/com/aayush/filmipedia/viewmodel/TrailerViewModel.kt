package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Trailer
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.TrailerDataFactory
import io.reactivex.disposables.CompositeDisposable

class TrailerViewModel(application: FilmipediaApplication, movieId: Long?) : ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var trailerLiveData: LiveData<PagedList<Trailer>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val trailerDataFactory = TrailerDataFactory(application, compositeDisposable, movieId)
        networkState = Transformations.switchMap(trailerDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        trailerLiveData = LivePagedListBuilder(trailerDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}