package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Genre;
import com.aayush.filmipedia.util.datasource.GenreDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class GenreDataFactory extends DataSource.Factory<Long, Genre> {
    private MutableLiveData<GenreDataSource> mutableLiveData;
    private GenreDataSource genreDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    public GenreDataFactory(FilmipediaApplication application,
                              CompositeDisposable compositeDisposable) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Genre> create() {
        genreDataSource = new GenreDataSource(application, compositeDisposable);
        mutableLiveData.postValue(genreDataSource);
        return genreDataSource;
    }

    public MutableLiveData<GenreDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
