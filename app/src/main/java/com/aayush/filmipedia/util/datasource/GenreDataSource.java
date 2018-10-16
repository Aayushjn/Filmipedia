package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Genre;
import com.aayush.filmipedia.util.NetworkState;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GenreDataSource extends PageKeyedDataSource<Long, Genre> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    public GenreDataSource(FilmipediaApplication application,
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
                            @NonNull LoadInitialCallback<Long, Genre> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi()
                .getMovieGenres(BuildConfig.THE_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(genreResponse -> {
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(genreResponse.getGenres(),
                            null, 1L);
                }, throwable -> networkState
                        .postValue(new NetworkState(NetworkState.Status.FAILED,
                                throwable.getMessage())))
        );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Genre> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Genre> callback) {

    }
}
