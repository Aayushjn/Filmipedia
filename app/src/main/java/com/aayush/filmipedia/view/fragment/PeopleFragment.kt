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
import com.aayush.filmipedia.model.PersonResult
import com.aayush.filmipedia.util.adapter.PersonAdapter
import com.aayush.filmipedia.viewmodel.PersonViewModel
import kotlinx.android.synthetic.main.fragment_people.*

class PeopleFragment: Fragment() {
    private lateinit var resultList: MutableList<PersonResult>
    private lateinit var personAdapter: PersonAdapter
    private lateinit var personViewModel: PersonViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        resultList = arrayListOf()
        personAdapter = PersonAdapter(context!!, resultList)
        personViewModel = PersonViewModel(FilmipediaApplication.getApplication(context!!), "pop")

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler_view_people.layoutManager = LinearLayoutManager(context)
        } else {
            recycler_view_people.layoutManager = GridLayoutManager(context, 2)
        }
        personViewModel.apply {
            this.personLiveData.observe(this@PeopleFragment, Observer(personAdapter::submitList))
            this.networkState.observe(this@PeopleFragment, Observer(personAdapter::setNetworkState))
        }
        recycler_view_people.adapter = personAdapter
    }

    companion object {
        fun newInstance() = PeopleFragment()
    }
}
