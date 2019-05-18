package com.aayush.filmipedia.util.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val type: String
): PageKeyedDataSource<Long, Movie>() {
    val networkState  = MutableLiveData<NetworkState>()
    private val initialLoading  = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        if ("pop" == type) {
            compositeDisposable.add(
                application.getRestApi()
                    .getPopularMovies(Utility.getNativeKey(), 1L, application.countryCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results) ->
                        initialLoading.postValue(NetworkState.LOADED)
                        networkState.postValue(NetworkState.LOADED)

                        callback.onResult(results!!, null, 1L)
                    }) { throwable ->
                        networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                    }
            )
        } else {
            compositeDisposable.add(
                application.getRestApi()
                    .getMoviesByGenre(Utility.getNativeKey(), 1L, Integer.valueOf(type), application.countryCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results) ->
                        initialLoading.postValue(NetworkState.LOADED)
                        networkState.postValue(NetworkState.LOADED)

                        callback.onResult(results!!, null, 1L)
                    }) { throwable ->
                        networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                    }
            )
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) = Unit

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        if ("pop" == type) {
            compositeDisposable.add(
                application.getRestApi()
                    .getPopularMovies(Utility.getNativeKey(), params.requestedLoadSize.toLong(), application.countryCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results, totalResults) ->
                        val nextKey = if (params.key == totalResults.toLong())
                            null
                        else
                            params.key + 1
                        callback.onResult(results!!, nextKey)
                        networkState.postValue(NetworkState.LOADED)
                    }) { throwable ->
                        networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                    }
            )
        } else {
            compositeDisposable.add(
                application.getRestApi()
                    .getMoviesByGenre(
                        Utility.getNativeKey(),
                        params.requestedLoadSize.toLong(),
                        Integer.valueOf(type),
                        application.countryCode
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results, totalResults) ->
                        val nextKey = if (params.key == totalResults.toLong())
                            null
                        else
                            params.key + 1
                        callback.onResult(results!!, nextKey)
                        networkState.postValue(NetworkState.LOADED)
                    }) { throwable ->
                        networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                    }
            )
        }
    }
}