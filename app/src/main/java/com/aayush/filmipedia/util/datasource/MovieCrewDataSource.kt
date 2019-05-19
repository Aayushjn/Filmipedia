package com.aayush.filmipedia.util.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCrew
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieCrewDataSource(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable, private val movieId: Long?
): PageKeyedDataSource<Long, MovieCrew>() {
    val networkState = MutableLiveData<NetworkState>()
    private val initialLoading = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, MovieCrew>) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            application.getRestApi().getMovieCredits(movieId, Utility.getNativeKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (_, _, crew) ->
                    initialLoading.postValue(NetworkState.LOADED)
                    networkState.postValue(NetworkState.LOADED)

                    callback.onResult(crew!!, null, 1L)
                }) { throwable ->
                    networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                }
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, MovieCrew>) = Unit

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, MovieCrew>) = Unit
}