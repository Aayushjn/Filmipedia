package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieResultDataSource extends PageKeyedDataSource<Long, Movie> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    private String query;

    public MovieResultDataSource(FilmipediaApplication application,
                                 CompositeDisposable compositeDisposable, String query) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.query = query;

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
                            @NonNull final LoadInitialCallback<Long, Movie> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi()
                .searchMovies(BuildConfig.THE_MOVIE_DB_API_KEY, query, 1L,
                        application.getCountryCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> {
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(movieResponse.getResults(),
                            null, 1L);
                    }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage())))
        );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params,
                          @NonNull final LoadCallback<Long, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi()
                .searchMovies(BuildConfig.THE_MOVIE_DB_API_KEY, query, (long) params.requestedLoadSize,
                        application.getCountryCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> {
                    Long nextKey = (Objects.equals(params.key, movieResponse.getTotalPages())) ?
                            null : params.key + 1L;
                    callback.onResult(movieResponse.getResults(), nextKey);
                    networkState.postValue(NetworkState.LOADED);
                }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage())))
        );
    }
}
