package com.aayush.filmipedia.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Genre
import com.aayush.filmipedia.util.adapter.GenreAdapter
import com.aayush.filmipedia.viewmodel.GenreViewModel
import kotlinx.android.synthetic.main.fragment_genre.*

class GenreFragment: Fragment() {
    private lateinit var genreList: List<Genre>
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var genreViewModel: GenreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        genreList = arrayListOf()
        genreAdapter = GenreAdapter(activity as AppCompatActivity)
        genreViewModel = GenreViewModel(FilmipediaApplication.getApplication(context!!))

        recycler_view_genre.layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, 2)
        }
        genreViewModel.apply {
            this.genreLiveData.observe(this@GenreFragment, Observer(genreAdapter::submitList))
            this.networkState.observe(this@GenreFragment, Observer(genreAdapter::setNetworkState))
        }
        recycler_view_genre.adapter = genreAdapter
    }

    companion object {
        fun newInstance() = GenreFragment()
    }
}
