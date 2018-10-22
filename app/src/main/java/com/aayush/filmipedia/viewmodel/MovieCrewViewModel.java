package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.MovieCrew;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.MovieCrewDataSource;
import com.aayush.filmipedia.util.datasource.factory.MovieCrewDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class MovieCrewViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<MovieCrew>> movieCrewLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public MovieCrewViewModel(@NonNull FilmipediaApplication application, Long movieId) {
        this.application = application;
        this.movieId = movieId;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        MovieCrewDataFactory movieCastDataFactory = new MovieCrewDataFactory(application,
                compositeDisposable, movieId);
        networkState = Transformations.switchMap(movieCastDataFactory.getMutableLiveData(),
                MovieCrewDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        movieCrewLiveData = new LivePagedListBuilder<>(movieCastDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<MovieCrew>> getMovieCrewLiveData() {
        return movieCrewLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
