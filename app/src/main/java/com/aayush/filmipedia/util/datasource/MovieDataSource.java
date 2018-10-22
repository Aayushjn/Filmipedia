package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDataSource extends PageKeyedDataSource<Long, Movie> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    private String type;

    public MovieDataSource(FilmipediaApplication application,
                           CompositeDisposable compositeDisposable, String type) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.type = type;

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

        if ("pop".equals(type)) {
            compositeDisposable.add(application.getRestApi()
                    .getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY, 1L,
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
        else {
            compositeDisposable.add(application.getRestApi()
                    .getMoviesByGenre(BuildConfig.THE_MOVIE_DB_API_KEY, 1L,
                            Integer.valueOf(type),
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
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params,
                          @NonNull final LoadCallback<Long, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);

        if ("pop".equals(type)) {
            compositeDisposable.add(application.getRestApi()
                    .getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY, (long) params.requestedLoadSize,
                            application.getCountryCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieResponse -> {
                        Long nextKey = (params.key == movieResponse.getTotalResults()) ?
                                null : params.key + 1;
                        callback.onResult(movieResponse.getResults(), nextKey);
                        networkState.postValue(NetworkState.LOADED);
                    }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                            throwable.getMessage())))
            );
        }
        else {
            compositeDisposable.add(application.getRestApi()
                    .getMoviesByGenre(BuildConfig.THE_MOVIE_DB_API_KEY, (long) params.requestedLoadSize,
                            Integer.valueOf(type), application.getCountryCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieResponse -> {
                        Long nextKey = (params.key == movieResponse.getTotalResults()) ?
                                null : params.key + 1;
                        callback.onResult(movieResponse.getResults(), nextKey);
                        networkState.postValue(NetworkState.LOADED);
                    }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                            throwable.getMessage())))
            );
        }
    }
}
