package com.aayush.filmipedia.util.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.model.PersonResult
import com.aayush.filmipedia.util.NetworkState
import com.aayush.filmipedia.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PersonDataSource(
    private val application: FilmipediaApplication,
    private val compositeDisposable: CompositeDisposable,
    private val query: String
): PageKeyedDataSource<Long, PersonResult>() {
    val networkState = MutableLiveData<NetworkState>()
    private val initialLoading = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, PersonResult>) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        if ("pop" == query) {
            compositeDisposable.add(application.getRestApi()
                .getPopularPeople(Utility.getNativeKey(), 1L)
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
            compositeDisposable.add(application.getRestApi()
                .searchPeople(Utility.getNativeKey(), query, 1L, application.countryCode)
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

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, PersonResult>) = Unit

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, PersonResult>) {
        networkState.postValue(NetworkState.LOADING)

        if ("pop" == query) {
            compositeDisposable.add(
                application.getRestApi()
                    .getPopularPeople(Utility.getNativeKey(), params.requestedLoadSize.toLong())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results, _, totalPages) ->
                        val nextKey = if (params.key == totalPages)
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
                    .searchPeople(
                        Utility.getNativeKey(),
                        query,
                        params.requestedLoadSize.toLong(),
                        application.countryCode
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (_, results, _, totalPages) ->
                        val nextKey = if (params.key == totalPages)
                            null
                        else
                            params.key + 1L
                        callback.onResult(results!!, nextKey)
                        networkState.postValue(NetworkState.LOADED)
                    }) { throwable ->
                        networkState.postValue(throwable.message?.let { NetworkState(NetworkState.Status.FAILED, it) })
                    }
            )
        }
    }
}