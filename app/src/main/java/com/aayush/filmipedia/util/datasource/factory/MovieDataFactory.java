package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.datasource.MovieDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class MovieDataFactory extends DataSource.Factory<Long, Movie> {
    private MutableLiveData<MovieDataSource> mutableLiveData;
    private MovieDataSource movieDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;
    private String type;

    public MovieDataFactory(FilmipediaApplication application,
                            CompositeDisposable compositeDisposable, String type) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.type = type;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Movie> create() {
        movieDataSource = new MovieDataSource(application, compositeDisposable, type);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
