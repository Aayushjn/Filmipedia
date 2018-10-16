package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Genre;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.GenreDataSource;
import com.aayush.filmipedia.util.datasource.factory.GenreDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class GenreViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Genre>> genreLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    public GenreViewModel(@NonNull FilmipediaApplication application) {
        this.application = application;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        GenreDataFactory genreDataFactory = new GenreDataFactory(application, compositeDisposable);
        networkState = Transformations.switchMap(genreDataFactory.getMutableLiveData(),
                GenreDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        genreLiveData = new LivePagedListBuilder<>(genreDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Genre>> getGenreLiveData() {
        return genreLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
