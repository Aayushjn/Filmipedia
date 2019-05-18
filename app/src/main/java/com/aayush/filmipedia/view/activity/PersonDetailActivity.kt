package com.aayush.filmipedia.view.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Person
import com.aayush.filmipedia.util.TAG_EXTRAS
import com.aayush.filmipedia.view.fragment.PersonDetailFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class PersonDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        val person = intent.getParcelableExtra<Person>(TAG_EXTRAS)

        val fragment = PersonDetailFragment.newInstance().apply {
            arguments = Bundle().apply { putParcelable(TAG_EXTRAS, person) }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
}
