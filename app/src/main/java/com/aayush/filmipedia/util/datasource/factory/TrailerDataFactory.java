package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Trailer;
import com.aayush.filmipedia.util.datasource.TrailerDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class TrailerDataFactory extends DataSource.Factory<Long, Trailer> {
    private MutableLiveData<TrailerDataSource> mutableLiveData;
    private TrailerDataSource trailerDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long movieId;

    public TrailerDataFactory(FilmipediaApplication application,
                              CompositeDisposable compositeDisposable, Long movieId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.movieId = movieId;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Trailer> create() {
        trailerDataSource = new TrailerDataSource(application, compositeDisposable, movieId);
        mutableLiveData.postValue(trailerDataSource);
        return trailerDataSource;
    }

    public MutableLiveData<TrailerDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
