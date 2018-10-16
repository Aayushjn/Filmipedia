package com.aayush.filmipedia.view.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Genre;
import com.aayush.filmipedia.util.adapter.GenreAdapter;
import com.aayush.filmipedia.viewmodel.GenreViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GenreFragment extends Fragment {
    @BindView(R.id.recycler_view_genre) RecyclerView recyclerView;

    private List<Genre> genreList;
    private GenreAdapter genreAdapter;
    private GenreViewModel genreViewModel;

    private Unbinder unbinder;

    public GenreFragment() {}

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        genreList = new ArrayList<>();
        genreAdapter = new GenreAdapter((AppCompatActivity) getActivity(), genreList);
        genreViewModel = new GenreViewModel(FilmipediaApplication.create(getContext()));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        genreViewModel.getGenreLiveData().observe(this, genreAdapter::submitList);
        genreViewModel.getNetworkState().observe(this, genreAdapter::setNetworkState);
        recyclerView.setAdapter(genreAdapter);
    }
}
