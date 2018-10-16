package com.aayush.filmipedia.util.datasource.factory;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.PersonResult;
import com.aayush.filmipedia.util.datasource.PersonDataSource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class PersonDataFactory extends DataSource.Factory<Long, PersonResult> {
    private MutableLiveData<PersonDataSource> mutableLiveData;
    private PersonDataSource personDataSource;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    public PersonDataFactory(FilmipediaApplication application,
                              CompositeDisposable compositeDisposable) {
        this.application = application;
        this.compositeDisposable = compositeDisposable;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Long, PersonResult> create() {
        personDataSource = new PersonDataSource(application, compositeDisposable);
        mutableLiveData.postValue(personDataSource);
        return personDataSource;
    }

    public MutableLiveData<PersonDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
