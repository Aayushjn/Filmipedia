package com.aayush.filmipedia.view.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.PersonResult;
import com.aayush.filmipedia.util.adapter.PersonAdapter;
import com.aayush.filmipedia.viewmodel.PersonViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PeopleFragment extends Fragment {
    @BindView(R.id.recycler_view_people) RecyclerView recyclerView;

    private List<PersonResult> resultList;
    private PersonAdapter personAdapter;
    private PersonViewModel personViewModel;

    private Unbinder unbinder;

    public PeopleFragment() {}

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        Objects.requireNonNull(
                ((AppCompatActivity) Objects.requireNonNull(getActivity()))
                        .getSupportActionBar()
        ).setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        resultList = new ArrayList<>();
        personAdapter = new PersonAdapter(getContext(), resultList);
        personViewModel = new PersonViewModel(FilmipediaApplication.create(getContext()), "pop");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        personViewModel.getPersonLiveData().observe(this,
                personResults -> personAdapter.submitList(personResults));
        personViewModel.getNetworkState().observe(this,
                networkState -> personAdapter.setNetworkState(networkState));
        recyclerView.setAdapter(personAdapter);
    }
}
