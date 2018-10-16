package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Crew;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.CrewDataSource;
import com.aayush.filmipedia.util.datasource.factory.CrewDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class CrewViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Crew>> crewLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long personId;

    public CrewViewModel(@NonNull FilmipediaApplication application, Long personId) {
        this.application = application;
        this.personId = personId;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        CrewDataFactory crewDataFactory = new CrewDataFactory(application, compositeDisposable, personId);
        networkState = Transformations.switchMap(crewDataFactory.getMutableLiveData(),
                CrewDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        crewLiveData = new LivePagedListBuilder<>(crewDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Crew>> getCrewLiveData() {
        return crewLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
