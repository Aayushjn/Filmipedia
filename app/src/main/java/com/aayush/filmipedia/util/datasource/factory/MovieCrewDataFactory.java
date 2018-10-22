package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.MovieCrew;
import com.aayush.filmipedia.util.datasource.MovieCrewDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class MovieCrewDataFactory extends DataSource.Factory<Long, MovieCrew> {
    private MutableLiveData<MovieCrewDataSource> mutableLiveData;
    private MovieCrewDataSource movieCrewDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public MovieCrewDataFactory(FilmipediaApplication application,
                                CompositeDisposable compositeDisposable, Long movieId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.movieId = movieId;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, MovieCrew> create() {
        movieCrewDataSource = new MovieCrewDataSource(application, compositeDisposable, movieId);
        mutableLiveData.postValue(movieCrewDataSource);
        return movieCrewDataSource;
    }

    public MutableLiveData<MovieCrewDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
