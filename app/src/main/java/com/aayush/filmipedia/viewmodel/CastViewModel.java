package com.aayush.filmipedia.viewmodel;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.model.Cast;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.datasource.CastDataSource;
import com.aayush.filmipedia.util.datasource.factory.CastDataFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

public class CastViewModel extends ViewModel {
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Cast>> castLiveData;
    private FilmipediaApplication application;
    private CompositeDisposable compositeDisposable;

    private Long personId;

    public CastViewModel(@NonNull FilmipediaApplication application, Long personId) {
        this.application = application;
        this.personId = personId;
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();

        CastDataFactory castDataFactory = new CastDataFactory(application, compositeDisposable, personId);
        networkState = Transformations.switchMap(castDataFactory.getMutableLiveData(),
                CastDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        castLiveData = new LivePagedListBuilder<>(castDataFactory, pagedListConfig)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Cast>> getCastLiveData() {
        return castLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
