package com.aayush.filmipedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCrew
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.datasource.factory.MovieCrewDataFactory
import io.reactivex.disposables.CompositeDisposable

class MovieCrewViewModel(application: FilmipediaApplication, movieId: Long?) : ViewModel() {
    var networkState: LiveData<NetworkState>
        private set
    var movieCrewLiveData: LiveData<PagedList<MovieCrew>>
        private set
    private var compositeDisposable = CompositeDisposable()

    init {
        val movieCastDataFactory = MovieCrewDataFactory(application, compositeDisposable, movieId)
        networkState = Transformations.switchMap(movieCastDataFactory.mutableLiveData) { it.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        movieCrewLiveData = LivePagedListBuilder(movieCastDataFactory, pagedListConfig)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}