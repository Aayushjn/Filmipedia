package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCast
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.MovieCastDataFactory
import io.reactivex.disposables.CompositeDisposable

class MovieCastViewModel(application: FilmipediaApplication, movieId: Long?) : ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var movieCastLiveData: LiveData<PagedList<MovieCast>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val movieCastDataFactory = MovieCastDataFactory(application, compositeDisposable, movieId)
        networkState = Transformations.switchMap(movieCastDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        movieCastLiveData = LivePagedListBuilder(movieCastDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}