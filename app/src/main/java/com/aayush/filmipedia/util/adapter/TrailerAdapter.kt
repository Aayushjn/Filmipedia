package com.aayush.filmipedia.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aayush.filmipedia.R
import com.aayush.filmipedia.model.Trailer
import com.aayush.filmipedia.util.*
import kotlinx.android.synthetic.main.card_trailer.view.*

data class TrailerAdapter(private val context: Context): ListAdapter<Trailer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_PROGRESS) {
            val view = inflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_trailer, parent, false)
            TrailerViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrailerViewHolder) {
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

    internal inner class TrailerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    watchYoutubeVideo(
                        getItem(adapterPosition).key!!,
                        context
                    )
                }
            }
        }

        fun bindTo(trailer: Trailer?) = trailer?.let { itemView.text_trailer_title.text = it.name }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Trailer>() {
            override fun areItemsTheSame(oldItem: Trailer, newItem: Trailer) = oldItem.key == newItem.key
            override fun areContentsTheSame(oldItem: Trailer, newItem: Trailer) = oldItem == newItem
        }
    }
}