package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.MovieResultDataSource;
import com.aayush.filmipedia.util.datasource.factory.MovieResultDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class MovieResultViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Movie>> movieLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;
    private String query;

    public MovieResultViewModel(@NonNull FilmipediaApplication application, String query) {
        this.application = application;
        this.query = query;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        MovieResultDataFactory movieResultDataFactory = new MovieResultDataFactory(application,
                compositeDisposable, query);

        networkState = Transformations.switchMap(movieResultDataFactory.getMutableLiveData(),
                MovieResultDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        movieLiveData = new LivePagedListBuilder<>(movieResultDataFactory, pagedListConfig)
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
