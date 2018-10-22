package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.MovieCast;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.MovieCastDataSource;
import com.aayush.filmipedia.util.datasource.factory.MovieCastDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class MovieCastViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<MovieCast>> movieCastLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public MovieCastViewModel(@NonNull FilmipediaApplication application, Long movieId) {
        this.application = application;
        this.movieId = movieId;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        MovieCastDataFactory movieCastDataFactory = new MovieCastDataFactory(application,
                compositeDisposable, movieId);
        networkState = Transformations.switchMap(movieCastDataFactory.getMutableLiveData(),
                MovieCastDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        movieCastLiveData = new LivePagedListBuilder<>(movieCastDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<MovieCast>> getMovieCastLiveData() {
        return movieCastLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
