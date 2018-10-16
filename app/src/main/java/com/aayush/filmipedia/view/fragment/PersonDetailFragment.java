package com.aayush.filmipedia.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.GlideApp;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Cast;
import com.aayush.filmipedia.model.Crew;
import com.aayush.filmipedia.model.Person;
import com.aayush.filmipedia.util.adapter.CastAdapter;
import com.aayush.filmipedia.util.adapter.CrewAdapter;
import com.aayush.filmipedia.viewmodel.CastViewModel;
import com.aayush.filmipedia.viewmodel.CrewViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class PersonDetailFragment extends Fragment {
    @BindView(R.id.recycler_view_cast)   RecyclerView castRecyclerView;
    @BindView(R.id.recycler_view_crew)   RecyclerView crewRecyclerView;
    @BindView(R.id.toolbar)              Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)   CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)               AppBarLayout appBarLayout;
    @BindView(R.id.img_thumbnail_header) ImageView posterImageView;
    @BindView(R.id.text_person_name)     TextView nameTextView;
    @BindView(R.id.text_person_about)    TextView aboutTextView;
    @BindView(R.id.text_person_birthday) TextView birthdayTextView;
    @BindView(R.id.text_person_deathday) TextView deathdayTextView;
    @BindView(R.id.text_deathday_label)  TextView deathdayLabel;

    private Person person;
    private List<Cast> castList;
    private List<Crew> crewList;
    private CastViewModel castViewModel;
    private CrewViewModel crewViewModel;

    private Unbinder unbinder;

    public PersonDetailFragment() {}

    public static PersonDetailFragment newInstance() {
        return new PersonDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        person = getArguments().getParcelable(EXTRAS_TAG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar())
                .setTitle("");

        initCollapsingToolbar();

        if (person != null) {
            nameTextView.setText(person.getName());
            aboutTextView.setText(person.getBiography());
            birthdayTextView.setText(person.getBirthday());
            if (person.getDeathday() == null) {
                deathdayLabel.setVisibility(View.GONE);
                deathdayTextView.setVisibility(View.GONE);
            }
            else {
                deathdayLabel.setVisibility(View.VISIBLE);
                deathdayTextView.setVisibility(View.VISIBLE);
                deathdayTextView.setText(person.getDeathday());
            }
            GlideApp.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + person.getProfilePath())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(posterImageView);
        }
        else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        castViewModel = new CastViewModel(FilmipediaApplication.create(getContext()), person.getId());
        crewViewModel = new CrewViewModel(FilmipediaApplication.create(getContext()), person.getId());

        initViews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                    collapsingToolbarLayout.setTitle(person.getName());
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
        castList = new ArrayList<>();
        crewList = new ArrayList<>();
        CastAdapter castAdapter = new CastAdapter(getContext(), castList);
        CrewAdapter crewAdapter = new CrewAdapter(getContext(), crewList);

        castRecyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        crewRecyclerView.setLayoutManager(
                new LinearLayoutManager(Objects.requireNonNull(getActivity())
                        .getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        castViewModel.getCastLiveData().observe(this, castAdapter::submitList);
        castViewModel.getNetworkState().observe(this, castAdapter::setNetworkState);

        crewViewModel.getCrewLiveData().observe(this, crewAdapter::submitList);
        crewViewModel.getNetworkState().observe(this, crewAdapter::setNetworkState);

        castRecyclerView.setAdapter(castAdapter);
        crewRecyclerView.setAdapter(crewAdapter);
    }
}
