package com.aayush.filmipedia.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aayush.filmipedia.R
import kotlinx.android.synthetic.main.fragment_browse.*

class BrowseFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_movie.background.alpha = 191

        setListeners()
    }

    private fun setListeners() {
        text_movie.setOnClickListener {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, GenreFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        text_people.setOnClickListener {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, PeopleFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance() = BrowseFragment()
    }
}
