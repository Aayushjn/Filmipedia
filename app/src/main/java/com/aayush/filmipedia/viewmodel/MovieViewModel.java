package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.MovieDataSource;
import com.aayush.filmipedia.util.datasource.factory.MovieDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class MovieViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Movie>> movieLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;
    private String type;

    public MovieViewModel(@NonNull FilmipediaApplication application, String type) {
        this.application = application;
        this.type = type;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        MovieDataFactory movieDataFactory = new MovieDataFactory(application,
                compositeDisposable, type);

        networkState = Transformations.switchMap(movieDataFactory.getMutableLiveData(),
                MovieDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        movieLiveData = new LivePagedListBuilder<>(movieDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Movie>> getMovieLiveData() {
        return movieLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
