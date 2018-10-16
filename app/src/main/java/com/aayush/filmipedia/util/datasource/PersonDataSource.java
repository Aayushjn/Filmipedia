package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.PersonResult;
import com.aayush.filmipedia.util.NetworkState;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PersonDataSource extends PageKeyedDataSource<Long, PersonResult> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    public PersonDataSource(FilmipediaApplication application,
                            CompositeDisposable compositeDisposable) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;

        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, PersonResult> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi()
                .getPopularPeople(BuildConfig.THE_MOVIE_DB_API_KEY, 1L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personResponse -> {
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(personResponse.getResults(),
                            null, 1L);
                }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage()))));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, PersonResult> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, PersonResult> callback) {
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi()
                .getPopularPeople(BuildConfig.THE_MOVIE_DB_API_KEY, (long) params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personResponse -> {
                    Long nextKey = (params.key == personResponse.getTotalPages()) ?
                            null : params.key + 1;
                    callback.onResult(personResponse.getResults(), nextKey);
                    networkState.postValue(NetworkState.LOADED);
                }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage())))
        );
    }
}
