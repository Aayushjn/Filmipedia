package com.aayush.filmipedia.view.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.aayush.filmipedia.R
import com.aayush.filmipedia.util.adapter.BottomNavAdapter
import com.aayush.filmipedia.view.fragment.BrowseFragment
import com.aayush.filmipedia.view.fragment.MainFragment
import com.aayush.filmipedia.view.fragment.SearchFragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    private lateinit var pagerAdapter: BottomNavAdapter

    private val navTitles = arrayOf("Browse", "Home", "Search")
    private val navIcons = intArrayOf(R.drawable.ic_browse, R.drawable.ic_home, R.drawable.ic_search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener(this)

        view_pager.isSwipeEnabled = false
        pagerAdapter = BottomNavAdapter(supportFragmentManager).apply {
            addFragment(BrowseFragment.newInstance())
            addFragment(MainFragment.newInstance())
            addFragment(SearchFragment.newInstance())
            view_pager.adapter = this
        }

        initBottomNavigation()
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))

    override fun onBackPressed() {
        if (bottom_nav.currentItem == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, BrowseFragment.newInstance())
                .commit()
        }
        if (bottom_nav.currentItem != 1) {
            bottom_nav.currentItem = 1
            shouldDisplayHomeAsUp()
        } else {
            super.onBackPressed()
        }
    }

    private fun initBottomNavigation() {
        val browseItem = AHBottomNavigationItem(navTitles[0], navIcons[0])
        val homeItem = AHBottomNavigationItem(navTitles[1], navIcons[1])
        val searchItem = AHBottomNavigationItem(navTitles[2], navIcons[2])

        bottom_nav.apply {
            addItem(browseItem)
            addItem(homeItem)
            addItem(searchItem)
            currentItem = 1
        }

        view_pager.currentItem = 1

        bottom_nav.apply {
            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) {
                    view_pager.currentItem = position
                    shouldDisplayHomeAsUp()
                }
                true
            }
            defaultBackgroundColor = ContextCompat.getColor(this@MainActivity, R.color.nav_background)
            accentColor = ContextCompat.getColor(this@MainActivity, R.color.nav_active)
            inactiveColor = ContextCompat.getColor(this@MainActivity, R.color.nav_inactive)
            isTranslucentNavigationEnabled = true
            titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
            isBehaviorTranslationEnabled = true
        }
    }

    override fun onBackStackChanged() = shouldDisplayHomeAsUp()

    private fun shouldDisplayHomeAsUp() {
        var canBack = supportFragmentManager.backStackEntryCount >= 0
        if (bottom_nav.currentItem != 0) {
            canBack = false
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(canBack)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount == 0) {
//                supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frag_container, BrowseFragment.newInstance())
//                    .commit()
                shouldDisplayHomeAsUp()
                onBackPressed()
            } else {
                supportFragmentManager.popBackStack()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
