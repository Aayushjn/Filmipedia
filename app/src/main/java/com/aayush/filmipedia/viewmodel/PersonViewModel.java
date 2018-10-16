package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.PersonResult;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.PersonDataSource;
import com.aayush.filmipedia.util.datasource.factory.PersonDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class PersonViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<PersonResult>> personLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    public PersonViewModel(@NonNull FilmipediaApplication application) {
        this.application = application;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        PersonDataFactory personDataFactory = new PersonDataFactory(application,
                compositeDisposable);
        networkState = Transformations.switchMap(personDataFactory.getMutableLiveData(),
                PersonDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        personLiveData = new LivePagedListBuilder<>(personDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<PersonResult>> getPersonLiveData() {
        return personLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
