package com.aayush.filmipedia.view.activity;

import android.content.Context;
import android.os.Bundle;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.view.fragment.DetailFragment;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = Objects.requireNonNull(getIntent().getExtras()).getParcelable(EXTRAS_TAG);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRAS_TAG, movie);

        Fragment fragment = DetailFragment.newInstance();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
