package com.aayush.filmipedia.util.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aayush.filmipedia.FilmipediaApplication
import com.aayush.filmipedia.GlideApp
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.MovieCast
import com.aayush.filmipedia.model.Person
import com.aayush.filmipedia.util.*
import com.aayush.filmipedia.view.activity.PersonDetailActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.card_known_for.view.*
import java.util.*

class MovieCastAdapter(private val context: Context): ListAdapter<MovieCast, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_PROGRESS) {
            val view = inflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_known_for, parent, false)
            MovieCastViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieCastViewHolder) {
            holder.bindTo(getItem(position))
        } else {
            (holder as NetworkStateViewHolder).bindView(networkState)
        }
    }

    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow = hasExtraRow()

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    internal inner class MovieCastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (isNetworkNotAvailable(context)) {
                        Snackbar.make(
                            view, "Connect to internet to view details!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        val movieCast = getItem(position)
                        val application = FilmipediaApplication.getApplication(context)
                        val person: Person

                        person = application.getRestApi()
                            .getPersonById(
                                Objects.requireNonNull(movieCast).id,
                                Utility.getNativeKey()
                            )
                            .subscribeOn(Schedulers.io())
                            .blockingFirst(null)

                        val intent = Intent(context, PersonDetailActivity::class.java)
                        intent.putExtra(TAG_EXTRAS, person)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            }
        }

        fun bindTo(movieCast: MovieCast?) =
            movieCast?.let {
                itemView.text_character.text = it.character
                itemView.text_movie_title.text = it.name
                GlideApp.with(context)
                    .load(IMAGE_URL + it.profilePath)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(itemView.img_movie_poster)
            }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<MovieCast>() {
            override fun areItemsTheSame(oldItem: MovieCast, newItem: MovieCast) = oldItem.castId == newItem.castId
            override fun areContentsTheSame(oldItem: MovieCast, newItem: MovieCast) = oldItem == newItem
        }
    }
}