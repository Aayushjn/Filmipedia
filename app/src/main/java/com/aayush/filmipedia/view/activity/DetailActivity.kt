package com.aayush.filmipedia.view.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.TAG_EXTRAS
import com.aayush.filmipedia.view.fragment.DetailFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class DetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movie = intent?.extras?.getParcelable<Movie>(TAG_EXTRAS)

        val fragment = DetailFragment.newInstance().apply {
            arguments = Bundle().apply { putParcelable(TAG_EXTRAS, movie) }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
}
