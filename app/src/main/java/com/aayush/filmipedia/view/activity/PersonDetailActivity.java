package com.aayush.filmipedia.view.activity;

import android.content.Context;
import android.os.Bundle;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Person;
import com.aayush.filmipedia.view.fragment.PersonDetailFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Person person = getIntent().getParcelableExtra(EXTRAS_TAG);
        Bundle args = new Bundle();
        args.putParcelable(EXTRAS_TAG, person);

        Fragment fragment = PersonDetailFragment.newInstance();
        fragment.setArguments(args);
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
