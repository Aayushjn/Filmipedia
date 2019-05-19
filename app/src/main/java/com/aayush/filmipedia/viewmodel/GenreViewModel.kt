package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Genre
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.GenreDataFactory
import io.reactivex.disposables.CompositeDisposable

class GenreViewModel(application: FilmipediaApplication): ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var genreLiveData: LiveData<PagedList<Genre>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val genreDataFactory = GenreDataFactory(application, compositeDisposable)
        networkState = Transformations.switchMap(genreDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        genreLiveData = LivePagedListBuilder(genreDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}