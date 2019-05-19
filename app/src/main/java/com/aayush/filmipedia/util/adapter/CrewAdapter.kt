package com.aayush.filmipedia.util.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aayush.filmipedia.GlideApp
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Crew
import com.aayush.filmipedia.model.Movie
import com.aayush.filmipedia.util.*
import com.aayush.filmipedia.view.activity.DetailActivity
import kotlinx.android.synthetic.main.card_known_for.view.*

class CrewAdapter(private val context: Context): ListAdapter<Crew, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_PROGRESS) {
            val view = inflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_known_for, parent, false)
            CrewViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CrewViewHolder) {
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

    internal inner class CrewViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val movie = Movie(getItem(adapterPosition))

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(TAG_EXTRAS, movie)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }

        fun bindTo(crew: Crew?) =
            crew?.let {
                itemView.text_movie_title.text = crew.title
                itemView.text_character.text = crew.job
                GlideApp.with(view)
                    .load(IMAGE_URL + crew.posterPath!!)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(itemView.img_movie_poster)
            }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Crew>() {
            override fun areItemsTheSame(oldItem: Crew, newItem: Crew) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Crew, newItem: Crew) = oldItem == newItem
        }
    }
}