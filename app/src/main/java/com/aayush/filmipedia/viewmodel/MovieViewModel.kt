package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.MovieDataFactory
import io.reactivex.disposables.CompositeDisposable

class MovieViewModel(application: FilmipediaApplication, type: String) : ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var movieLiveData: LiveData<PagedList<Movie>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val movieDataFactory = MovieDataFactory(application, compositeDisposable, type)

        networkState = Transformations.switchMap(movieDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        movieLiveData = LivePagedListBuilder(movieDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}