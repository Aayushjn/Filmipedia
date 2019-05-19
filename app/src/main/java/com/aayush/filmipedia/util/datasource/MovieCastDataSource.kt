package com.aayush.filmipedia.util.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.MovieCast
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieCastDataSource(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val movieId: Long?
): PageKeyedDataSource<Long, MovieCast>() {
    val networkState = MutableLiveData<NetworkState>()
    private val initialLoading = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, MovieCast>) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            application.getRestApi().getMovieCredits(movieId, Utility.getNativeKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (_, cast) ->
                    initialLoading.postValue(NetworkState.LOADED)
                    networkState.postValue(NetworkState.LOADED)

                    callback.onResult(cast!!, null, 1L)
                }) { throwable ->
                    networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                }
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, MovieCast>) = Unit

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, MovieCast>) = Unit
}