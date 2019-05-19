package com.aayush.filmipedia.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.R
import com.aayush.filmipedia.util.TAG_EXTRAS
import com.aayush.filmipedia.util.adapter.MovieAdapter
import com.aayush.filmipedia.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment() {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    private var positionIndex = 0
    private var topView = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)

        movieViewModel = if (arguments == null) {
            MovieViewModel(
                FilmipediaApplication.getApplication(context!!),
                "pop"
            )
        } else {
            val genreId = arguments!!.getLong(TAG_EXTRAS)
            MovieViewModel(
                FilmipediaApplication.getApplication(context!!),
                genreId.toString()
            )
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        main_content.setColorSchemeResources(android.R.color.holo_orange_dark)
        main_content.setOnRefreshListener {
            initViews()
            Toast.makeText(context, "Movies refreshed!", Toast.LENGTH_SHORT).show()
            activity?.runOnUiThread { main_content.isRefreshing = false }
        }
    }

    override fun onPause() {
        super.onPause()

        positionIndex = (movie_list_recycler_view.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
        val startView = movie_list_recycler_view.getChildAt(positionIndex)
        topView = if (startView == null) 0 else startView.top - movie_list_recycler_view.paddingTop
    }

    override fun onResume() {
        super.onResume()

        if (positionIndex != -1) {
            (movie_list_recycler_view.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(positionIndex, topView)
        }
    }

    private fun initViews() {
        movieAdapter = MovieAdapter(context!!)

        movie_list_recycler_view.layoutManager = if (resources.configuration.orientation ==
            Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, 2)
        }
        movieViewModel.apply {
            movieLiveData.observe(this@MainFragment, Observer(movieAdapter::submitList))
            networkState.observe(this@MainFragment, Observer(movieAdapter::setNetworkState))
        }
        movie_list_recycler_view.adapter = movieAdapter
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
