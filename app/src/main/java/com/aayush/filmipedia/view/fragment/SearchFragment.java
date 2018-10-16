package com.aayush.filmipedia.view.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.adapter.MovieResultsAdapter;
import com.aayush.filmipedia.viewmodel.MovieResultViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment {
    @BindView(R.id.search_view)     SearchView searchView;
    @BindView(R.id.movie_results)   RecyclerView recyclerView;

    private MovieResultViewModel movieResultViewModel;
    private MovieResultsAdapter movieResultsAdapter;
    private List<Movie> movieList;
    private Unbinder unbinder;

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);

        initViews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViews() {
        movieResultViewModel = new MovieResultViewModel(FilmipediaApplication.create(getContext()), "");
        movieList = new ArrayList<>();
        movieResultsAdapter = new MovieResultsAdapter(getContext(), movieList);

        initSearch();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieResultViewModel = new MovieResultViewModel(FilmipediaApplication.create(getContext()), "");
        movieResultViewModel.getMovieLiveData().observe(SearchFragment.this,
                pagedList -> movieResultsAdapter.submitList(pagedList));
        movieResultViewModel.getNetworkState().observe(SearchFragment.this,
                networkState -> movieResultsAdapter.setNetworkState(networkState));
        recyclerView.setAdapter(movieResultsAdapter);

    }

    private void initSearch() {
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity()
                        .getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3) {
                    performSearch(newText);
                }
                return false;
            }
        });
    }

    private void performSearch(String query) {
        movieResultsAdapter.clear();
        movieResultViewModel = new MovieResultViewModel(FilmipediaApplication.create(getContext()),
                query);
        movieResultViewModel.getMovieLiveData().observe(this,
                movies -> movieResultsAdapter.submitList(movies));
        movieResultViewModel.getNetworkState().observe(this,
                networkState -> movieResultsAdapter.setNetworkState(networkState));
    }
}
