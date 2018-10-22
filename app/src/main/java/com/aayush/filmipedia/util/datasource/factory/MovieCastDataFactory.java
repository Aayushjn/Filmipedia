package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.MovieCast;
import com.aayush.filmipedia.util.datasource.MovieCastDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class MovieCastDataFactory extends DataSource.Factory<Long, MovieCast> {
    private MutableLiveData<MovieCastDataSource> mutableLiveData;
    private MovieCastDataSource movieCastDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public MovieCastDataFactory(FilmipediaApplication application,
                              CompositeDisposable compositeDisposable, Long movieId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.movieId = movieId;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, MovieCast> create() {
        movieCastDataSource = new MovieCastDataSource(application, compositeDisposable, movieId);
        mutableLiveData.postValue(movieCastDataSource);
        return movieCastDataSource;
    }

    public MutableLiveData<MovieCastDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
