package com.aayush.filmipedia.util.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Genre
import com.aayush.filmipedia.util.*
import com.aayush.filmipedia.view.fragment.MainFragment
import kotlinx.android.synthetic.main.card_genre.view.*

class GenreAdapter(private val activity: AppCompatActivity): ListAdapter<Genre, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_PROGRESS) {
            val view = inflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_genre, parent, false)
            GenreViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GenreViewHolder) {
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

    internal inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val (id) = getItem(position)
                    val fragment = MainFragment.newInstance()
                    val args = Bundle()
                    args.putLong(TAG_EXTRAS, id!!)
                    fragment.arguments = args

                    activity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        fun bindTo(genre: Genre?) = genre?.let { itemView.text_genre.text = it.name }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Genre, newItem: Genre) = oldItem == newItem
        }
    }
}