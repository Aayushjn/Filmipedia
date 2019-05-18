package com.aayush.filmipedia.view.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.model.PersonResult
import com.aayush.filmipedia.util.adapter.MovieResultsAdapter
import com.aayush.filmipedia.util.adapter.PersonAdapter
import com.aayush.filmipedia.viewmodel.MovieResultViewModel
import com.aayush.filmipedia.viewmodel.PersonViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: Fragment() {
    private lateinit var movieResultViewModel: MovieResultViewModel
    private lateinit var movieResultsAdapter: MovieResultsAdapter
    private lateinit var movieList: MutableList<Movie>
    private lateinit var personViewModel: PersonViewModel
    private lateinit var personAdapter: PersonAdapter
    private lateinit var personResultList: MutableList<PersonResult>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        btn_movie.setOnClickListener {
            btn_person.isChecked = !btn_movie.isChecked
            movieList = arrayListOf()
            movieResultViewModel = MovieResultViewModel(
                FilmipediaApplication.getApplication(context!!),
                ""
            ).apply {
                this.movieLiveData.observe(this@SearchFragment, Observer(movieResultsAdapter::submitList))
                this.networkState.observe(this@SearchFragment, Observer(movieResultsAdapter::setNetworkState))
            }
            movieResultsAdapter = MovieResultsAdapter(context!!, movieList).apply { movie_results.adapter = this }
        }
        btn_person.setOnClickListener {
            btn_movie.isChecked = !btn_person.isChecked
            personViewModel = PersonViewModel(FilmipediaApplication.getApplication(context!!), "")
            personResultList = arrayListOf()
            personAdapter = PersonAdapter(context!!, personResultList)
            personViewModel.apply {
                personLiveData.observe(this@SearchFragment, Observer(personAdapter::submitList))
                networkState.observe(this@SearchFragment, Observer(personAdapter::setNetworkState))
            }
            movie_results.adapter = personAdapter
        }

        movieList = arrayListOf()
        movieResultViewModel = MovieResultViewModel(FilmipediaApplication.getApplication(context!!), "")
        movieResultsAdapter = MovieResultsAdapter(context!!, movieList)
        personViewModel = PersonViewModel(FilmipediaApplication.getApplication(context!!), "")
        personResultList = arrayListOf()
        personAdapter = PersonAdapter(context!!, personResultList)

        initSearch()

        movie_results.layoutManager = LinearLayoutManager(context!!)

        if (btn_movie.isChecked) {
            movieResultViewModel.apply {
                movieLiveData.observe(this@SearchFragment, Observer(movieResultsAdapter::submitList))
                networkState.observe(this@SearchFragment, Observer(movieResultsAdapter::setNetworkState))
            }
            movie_results.adapter = movieResultsAdapter
        } else {
            personViewModel.apply {
                personLiveData.observe(this@SearchFragment, Observer(personAdapter::submitList))
                networkState.observe(this@SearchFragment, Observer(personAdapter::setNetworkState))
            }
            movie_results.adapter = personAdapter
        }
    }

    private fun initSearch() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            setIconifiedByDefault(false)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    this@apply.clearFocus()
                    performSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.length > 3) {
                        performSearch(newText)
                    }
                    return false
                }
            })
        }
    }

    private fun performSearch(query: String) {
        if (btn_movie.isChecked) {
            movieResultsAdapter.clear()
            movieResultViewModel = MovieResultViewModel(FilmipediaApplication.getApplication(context!!), query).apply { 
                this.movieLiveData.observe(this@SearchFragment, Observer(movieResultsAdapter::submitList))
                this.networkState.observe(this@SearchFragment, Observer(movieResultsAdapter::setNetworkState))
            }
        } else {
            personAdapter.clear()
            personViewModel = PersonViewModel(FilmipediaApplication.getApplication(context!!), query).apply {
                this.personLiveData.observe(this@SearchFragment, Observer(personAdapter::submitList))
                this.networkState.observe(this@SearchFragment, Observer(personAdapter::setNetworkState))   
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
