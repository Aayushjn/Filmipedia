package com.aayush.filmipedia.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.GlideApp;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.model.MovieCast;
import com.aayush.filmipedia.model.MovieCrew;
import com.aayush.filmipedia.model.Trailer;
import com.aayush.filmipedia.util.adapter.MovieCastAdapter;
import com.aayush.filmipedia.util.adapter.MovieCrewAdapter;
import com.aayush.filmipedia.util.adapter.TrailerAdapter;
import com.aayush.filmipedia.viewmodel.MovieCastViewModel;
import com.aayush.filmipedia.viewmodel.MovieCrewViewModel;
import com.aayush.filmipedia.viewmodel.TrailerViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class DetailFragment extends Fragment {
    @BindView(R.id.recycler_view_trailer) RecyclerView trailerRecyclerView;
    @BindView(R.id.recycler_view_cast)    RecyclerView castRecyclerView;
    @BindView(R.id.recycler_view_crew)    RecyclerView crewRecyclerView;
    @BindView(R.id.toolbar)               Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)                AppBarLayout appBarLayout;
    @BindView(R.id.img_thumbnail_header)  ImageView posterImageView;
    @BindView(R.id.text_movie_title)      TextView movieTitleTextView;
    @BindView(R.id.text_movie_overview)   TextView movieOverviewTextView;
    @BindView(R.id.text_movie_rating)     TextView movieRatingTextView;
    @BindView(R.id.text_release_date)     TextView releaseDateTextView;

    private Movie movie;
    private TrailerViewModel trailerViewModel;
    private MovieCastViewModel movieCastViewModel;
    private MovieCrewViewModel movieCrewViewModel;
    private Unbinder unbinder;

    public DetailFragment() {}

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        movie = getArguments().getParcelable(EXTRAS_TAG);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar())
                .setTitle("");

        initCollapsingToolbar();

        if (movie != null) {
            movieTitleTextView.setText(movie.getTitle());
            movieOverviewTextView.setText(movie.getOverview());
            movieRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
            releaseDateTextView.setText(movie.getReleaseDate());

            GlideApp.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(posterImageView);
        }
        else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        trailerViewModel = new TrailerViewModel(FilmipediaApplication.create(getContext()),
                movie.getId());
        movieCastViewModel = new MovieCastViewModel(FilmipediaApplication.create(getContext()),
                movie.getId());
        movieCrewViewModel = new MovieCrewViewModel(FilmipediaApplication.create(getContext()),
                movie.getId());

        initViews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initCollapsingToolbar() {
        collapsingToolbarLayout.setTitle("");
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(movie.getTitle());
                    isShown = true;
                }
                else if (isShown) {
                    collapsingToolbarLayout.setTitle("");
                    isShown = false;
                }
            }
        });
    }

    private void initViews() {
        List<Trailer> trailerList = new ArrayList<>();
        List<MovieCast> movieCastList = new ArrayList<>();
        List<MovieCrew> movieCrewList = new ArrayList<>();
        TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(), trailerList);
        MovieCastAdapter movieCastAdapter = new MovieCastAdapter(getContext(), movieCastList);
        MovieCrewAdapter movieCrewAdapter = new MovieCrewAdapter(getContext(), movieCrewList);

        trailerRecyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        trailerViewModel.getTrailerLiveData().observe(this, trailerAdapter::submitList);
        trailerViewModel.getNetworkState().observe(this, trailerAdapter::setNetworkState);
        trailerRecyclerView.setAdapter(trailerAdapter);

        castRecyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        movieCastViewModel.getMovieCastLiveData().observe(this, movieCastAdapter::submitList);
        movieCastViewModel.getNetworkState().observe(this, trailerAdapter::setNetworkState);
        castRecyclerView.setAdapter(movieCastAdapter);

        crewRecyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        movieCrewViewModel.getMovieCrewLiveData().observe(this, movieCrewAdapter::submitList);
        movieCrewViewModel.getNetworkState().observe(this, trailerAdapter::setNetworkState);
        crewRecyclerView.setAdapter(movieCrewAdapter);
    }
}
