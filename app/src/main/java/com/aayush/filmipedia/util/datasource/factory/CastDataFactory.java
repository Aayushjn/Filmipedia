package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Cast;
import com.aayush.filmipedia.util.datasource.CastDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class CastDataFactory extends DataSource.Factory<Long, Cast> {
    private MutableLiveData<CastDataSource> mutableLiveData;
    private CastDataSource castDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long personId;

    public CastDataFactory(FilmipediaApplication application,
                           CompositeDisposable compositeDisposable, Long personId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.personId = personId;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Cast> create() {
        castDataSource = new CastDataSource(application, compositeDisposable, personId);
        mutableLiveData.postValue(castDataSource);
        return castDataSource;
    }

    public MutableLiveData<CastDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
