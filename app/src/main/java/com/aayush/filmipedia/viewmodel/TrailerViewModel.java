package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Trailer;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.TrailerDataSource;
import com.aayush.filmipedia.util.datasource.factory.TrailerDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class TrailerViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Trailer>> trailerLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public TrailerViewModel(@NonNull FilmipediaApplication application, Long movieId) {
        this.application = application;
        this.movieId = movieId;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        TrailerDataFactory trailerDataFactory = new TrailerDataFactory(application, compositeDisposable, movieId);
        networkState = Transformations.switchMap(trailerDataFactory.getMutableLiveData(),
                TrailerDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        trailerLiveData = new LivePagedListBuilder<>(trailerDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Trailer>> getTrailerLiveData() {
        return trailerLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
