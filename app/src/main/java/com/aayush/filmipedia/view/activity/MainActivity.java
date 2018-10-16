package com.aayush.filmipedia.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.util.adapter.BottomNavAdapter;
import com.aayush.filmipedia.util.viewpager.NoSwipePager;
import com.aayush.filmipedia.view.fragment.BrowseFragment;
import com.aayush.filmipedia.view.fragment.MainFragment;
import com.aayush.filmipedia.view.fragment.SearchFragment;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener {
    @BindView(R.id.bottom_nav)  AHBottomNavigation bottomNavigation;
    @BindView(R.id.view_pager) NoSwipePager viewPager;

    private BottomNavAdapter pagerAdapter;

    private String[] navTitles = {"Browse", "Home", "Search"};
    private int[] navIcons = {R.drawable.ic_browse, R.drawable.ic_home, R.drawable.ic_search};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        Fragment homeFragment = MainFragment.newInstance();
        Fragment browseFragment = BrowseFragment.newInstance();
        Fragment searchFragment = SearchFragment.newInstance();

        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomNavAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(browseFragment);
        pagerAdapter.addFragment(homeFragment);
        pagerAdapter.addFragment(searchFragment);
        viewPager.setAdapter(pagerAdapter);

        initBottomNavigation();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigation.getCurrentItem() == 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag_container, BrowseFragment.newInstance())
                    .commit();
        }
        if (bottomNavigation.getCurrentItem() != 1) {
            bottomNavigation.setCurrentItem(1);
            shouldDisplayHomeAsUp();
        }
        else {
            super.onBackPressed();
        }
    }

    private void initBottomNavigation() {
        AHBottomNavigationItem browseItem = new AHBottomNavigationItem(navTitles[0], navIcons[0]);
        AHBottomNavigationItem homeItem = new AHBottomNavigationItem(navTitles[1], navIcons[1]);
        AHBottomNavigationItem searchItem = new AHBottomNavigationItem(navTitles[2], navIcons[2]);

        bottomNavigation.addItem(browseItem);
        bottomNavigation.addItem(homeItem);
        bottomNavigation.addItem(searchItem);

        bottomNavigation.setCurrentItem(1);
        viewPager.setCurrentItem(1);

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (!wasSelected) {
                viewPager.setCurrentItem(position);
                shouldDisplayHomeAsUp();
            }
            return true;
        });

        bottomNavigation.setDefaultBackgroundColor(ContextCompat
                .getColor(this, R.color.nav_background));
        bottomNavigation.setAccentColor(ContextCompat
                .getColor(this, R.color.nav_active));
        bottomNavigation.setInactiveColor(ContextCompat
                .getColor(this, R.color.nav_inactive));
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeAsUp();
    }

    private void shouldDisplayHomeAsUp() {
        boolean canBack;

        canBack = getSupportFragmentManager().getBackStackEntryCount() >= 0;
        if (bottomNavigation.getCurrentItem() != 0) {
            canBack = false;
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(canBack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.frag_container, BrowseFragment.newInstance())
//                        .commit();
                shouldDisplayHomeAsUp();
                onBackPressed();
            }
            else {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
