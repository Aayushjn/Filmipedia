package com.aayush.filmipedia.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.GlideApp
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Person
import com.aayush.filmipedia.util.IMAGE_URL
import com.aayush.filmipedia.util.TAG_EXTRAS
import com.aayush.filmipedia.util.adapter.CastAdapter
import com.aayush.filmipedia.util.adapter.CrewAdapter
import com.aayush.filmipedia.viewmodel.CastViewModel
import com.aayush.filmipedia.viewmodel.CrewViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_person_detail.*
import kotlinx.android.synthetic.main.fragment_person_detail.*

class PersonDetailFragment: Fragment() {
    private var person: Person? = null
    private lateinit var castViewModel: CastViewModel
    private lateinit var crewViewModel: CrewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        person = arguments?.getParcelable(TAG_EXTRAS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_person_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }

        initCollapsingToolbar()

        if (person != null) {
            text_person_name.text = person!!.name
            text_person_about.text = person!!.biography
            text_person_birthday.text = person!!.birthday
            if (person!!.deathday == null) {
                text_deathday_label.visibility = View.GONE
                text_person_deathday.visibility = View.GONE
            } else {
                text_deathday_label.visibility = View.VISIBLE
                text_person_deathday.visibility = View.VISIBLE
                text_person_deathday.text = person!!.deathday
            }
            GlideApp.with(this)
                .load(IMAGE_URL + person!!.profilePath)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(img_thumbnail_header)
        } else {
            Toast.makeText(context!!, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        castViewModel = CastViewModel(FilmipediaApplication.getApplication(context!!), person?.id)
        crewViewModel = CrewViewModel(FilmipediaApplication.getApplication(context!!), person?.id)

        initViews()
    }

    private fun initCollapsingToolbar() {
        collapsing_toolbar.title = ""
        appbar.setExpanded(true)

        appbar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
            var isShown = true
            var scrollRange = -1

            override fun onOffsetChanged(appbar: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appbar.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = person?.name
                    isShown = true
                } else if (isShown) {
                    collapsing_toolbar.title = ""
                    isShown = false
                }
            }
        })
    }

    private fun initViews() {
        val castAdapter = CastAdapter(context!!)
        val crewAdapter = CrewAdapter(context!!)

        recycler_view_cast.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recycler_view_crew.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        castViewModel.apply {
            this.castLiveData.observe(this@PersonDetailFragment, Observer(castAdapter::submitList))
            this.networkState.observe(this@PersonDetailFragment, Observer(castAdapter::setNetworkState))
        }

        crewViewModel.apply {
            this.crewLiveData.observe(this@PersonDetailFragment, Observer(crewAdapter::submitList))
            this.networkState.observe(this@PersonDetailFragment, Observer(crewAdapter::setNetworkState))
        }

        recycler_view_cast.adapter = castAdapter
        recycler_view_crew.adapter = crewAdapter
    }

    companion object {
        fun newInstance() = PersonDetailFragment()
    }
}
