package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.datasource.MovieResultDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class MovieResultDataFactory extends DataSource.Factory<Long, Movie> {
    private MutableLiveData<MovieResultDataSource> mutableLiveData;
    private MovieResultDataSource movieResultDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;
    private String type;

    public MovieResultDataFactory(FilmipediaApplication application,
                                  CompositeDisposable compositeDisposable, String type) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.type = type;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Movie> create() {
        movieResultDataSource = new MovieResultDataSource(application, compositeDisposable, type);
        mutableLiveData.postValue(movieResultDataSource);
        return movieResultDataSource;
    }

    public MutableLiveData<MovieResultDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
