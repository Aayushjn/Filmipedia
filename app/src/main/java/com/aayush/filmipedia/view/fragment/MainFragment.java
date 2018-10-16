package com.aayush.filmipedia.view.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.adapter.MovieAdapter;
import com.aayush.filmipedia.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class MainFragment extends Fragment {
    @BindView(R.id.main_content)             SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.movie_list_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_empty_view)          TextView emptyStateTextView;

    private Unbinder unbinder;

    private List<Movie> movieList;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    private Bundle args;

    private int positionIndex;
    private int topView;

    public MainFragment() {}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        if (args == null) {
            movieViewModel = new MovieViewModel(FilmipediaApplication.create(getContext()),
                    "pop");
        }
        else {
            long genreId = args.getLong(EXTRAS_TAG);
            movieViewModel = new MovieViewModel(FilmipediaApplication.create(getContext()),
                    String.valueOf(genreId));
        }

        initViews();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initViews();
            Toast.makeText(MainFragment.this.getContext(), "Movies refreshed!",
                    Toast.LENGTH_SHORT)
                    .show();
            Objects.requireNonNull(MainFragment.this.getActivity())
                    .runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (args == null) {
            inflater.inflate(R.menu.menu_main, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_menu_settings:
                // TODO: Add settings menu
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();

        positionIndex = ((LinearLayoutManager) Objects.requireNonNull(recyclerView
                .getLayoutManager()))
                .findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(positionIndex);
        topView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (positionIndex != -1) {
            ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                    .scrollToPositionWithOffset(positionIndex, topView);
        }
    }

    private void initViews() {
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext(), movieList);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        movieViewModel.getMovieLiveData().observe(this,
                movies -> movieAdapter.submitList(movies));
        movieViewModel.getNetworkState().observe(this,
                networkState -> movieAdapter.setNetworkState(networkState));
        recyclerView.setAdapter(movieAdapter);
    }
}
