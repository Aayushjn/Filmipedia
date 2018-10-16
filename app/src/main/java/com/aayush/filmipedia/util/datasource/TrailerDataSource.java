package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Trailer;
import com.aayush.filmipedia.util.NetworkState;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrailerDataSource extends PageKeyedDataSource<Long, Trailer> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public TrailerDataSource(FilmipediaApplication application,
                             CompositeDisposable compositeDisposable, Long movieId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.movieId = movieId;

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
                            @NonNull LoadInitialCallback<Long, Trailer> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi().getMovieTrailers(movieId,
                BuildConfig.THE_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerResponse -> {
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(trailerResponse.getResults(),
                            null, 1L);
                }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage())))
        );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Trailer> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Trailer> callback) {

    }
}
