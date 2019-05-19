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
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.IMAGE_URL
import com.aayush.filmipedia.util.TAG_EXTRAS
import com.aayush.filmipedia.util.adapter.MovieCastAdapter
import com.aayush.filmipedia.util.adapter.MovieCrewAdapter
import com.aayush.filmipedia.util.adapter.TrailerAdapter
import com.aayush.filmipedia.viewmodel.MovieCastViewModel
import com.aayush.filmipedia.viewmodel.MovieCrewViewModel
import com.aayush.filmipedia.viewmodel.TrailerViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment: Fragment() {
    private var movie: Movie? = null
    private lateinit var trailerViewModel: TrailerViewModel
    private lateinit var movieCastViewModel: MovieCastViewModel
    private lateinit var movieCrewViewModel: MovieCrewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        movie = arguments?.getParcelable(TAG_EXTRAS)

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with((activity as AppCompatActivity)) {
            setSupportActionBar(toolbar)
            supportActionBar!!.apply {
                setDisplayHomeAsUpEnabled(true)
                title = ""
            }
        }

        initCollapsingToolbar()

        if (movie != null) {
            text_movie_title.text = movie!!.title
            text_movie_overview.text = movie!!.overview
            text_movie_rating.text = movie!!.voteAverage.toString()
            text_release_date.text = movie!!.releaseDate

            GlideApp.with(this)
                .load(IMAGE_URL + movie!!.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(img_thumbnail_header)
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        trailerViewModel = TrailerViewModel(FilmipediaApplication.getApplication(context!!), movie?.id)
        movieCastViewModel = MovieCastViewModel(FilmipediaApplication.getApplication(context!!), movie?.id)
        movieCrewViewModel = MovieCrewViewModel(FilmipediaApplication.getApplication(context!!), movie?.id)

        initViews()
    }

    private fun initCollapsingToolbar() {
        collapsing_toolbar.title = ""
        appbar.setExpanded(true)

        appbar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
            var isShown = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = movie?.title
                    isShown = true
                } else if (isShown) {
                    collapsing_toolbar.title = ""
                    isShown = false
                }
            }
        })
    }

    private fun initViews() {
        val trailerAdapter = TrailerAdapter(context!!)
        val movieCastAdapter = MovieCastAdapter(context!!)
        val movieCrewAdapter = MovieCrewAdapter(context!!)

        recycler_view_trailer.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        trailerViewModel.apply {
            this.trailerLiveData.observe(this@DetailFragment, Observer(trailerAdapter::submitList))
            this.networkState.observe(this@DetailFragment, Observer(trailerAdapter::setNetworkState))
        }
        recycler_view_trailer.adapter = trailerAdapter

        recycler_view_cast.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        movieCastViewModel.apply {
            this.movieCastLiveData.observe(this@DetailFragment, Observer(movieCastAdapter::submitList))
            this.networkState.observe(this@DetailFragment, Observer(trailerAdapter::setNetworkState))
        }
        recycler_view_cast.adapter = movieCastAdapter

        recycler_view_crew.layoutManager = LinearLayoutManager(
            activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        movieCrewViewModel.apply {
            this.movieCrewLiveData.observe(this@DetailFragment, Observer { movieCrewAdapter.submitList(it) })
            this.networkState.observe(this@DetailFragment, Observer(trailerAdapter::setNetworkState))
        }
        recycler_view_crew.adapter = movieCrewAdapter
    }

    companion object {
        fun newInstance() = DetailFragment()
    }
}
