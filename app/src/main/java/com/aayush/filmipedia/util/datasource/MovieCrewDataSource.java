package com.aayush.filmipedia.util.datasource;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.MovieCrew;
import com.aayush.filmipedia.util.NetworkState;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieCrewDataSource extends PageKeyedDataSource<Long, MovieCrew> {
    private FilmipediaApplication application;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public MovieCrewDataSource(FilmipediaApplication application,
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
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Long> params,
                            @NonNull PageKeyedDataSource.LoadInitialCallback<Long, MovieCrew> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        compositeDisposable.add(application.getRestApi().getMovieCredits(movieId,
                BuildConfig.THE_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieCreditsResponse -> {
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(movieCreditsResponse.getCrew(),
                            null, 1L);
                }, throwable -> networkState.postValue(new NetworkState(NetworkState.Status.FAILED,
                        throwable.getMessage())))
        );
    }

    @Override
    public void loadBefore(@NonNull PageKeyedDataSource.LoadParams<Long> params,
                           @NonNull PageKeyedDataSource.LoadCallback<Long, MovieCrew> callback) {

    }

    @Override
    public void loadAfter(@NonNull PageKeyedDataSource.LoadParams<Long> params,
                          @NonNull PageKeyedDataSource.LoadCallback<Long, MovieCrew> callback) {

    }
}
