package com.aayush.filmipedia.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aayush.filmipedia.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BrowseFragment extends Fragment {
    private String extra;

    private Unbinder unbinder;

    public BrowseFragment() {}

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.text_movie, R.id.text_people})
    public void onClick(View view) {
        Fragment fragment;
        if (view.getId() == R.id.text_movie) {
            fragment = GenreFragment.newInstance();
        }
        else {
            fragment = PeopleFragment.newInstance();
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
