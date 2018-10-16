package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Crew;
import com.aayush.filmipedia.util.datasource.CrewDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class CrewDataFactory extends DataSource.Factory<Long, Crew> {
    private MutableLiveData<CrewDataSource> mutableLiveData;
    private CrewDataSource crewDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long personId;

    public CrewDataFactory(FilmipediaApplication application,
                           CompositeDisposable compositeDisposable, Long personId) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        this.personId = personId;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, Crew> create() {
        crewDataSource = new CrewDataSource(application, compositeDisposable, personId);
        mutableLiveData.postValue(crewDataSource);
        return crewDataSource;
    }

    public MutableLiveData<CrewDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
