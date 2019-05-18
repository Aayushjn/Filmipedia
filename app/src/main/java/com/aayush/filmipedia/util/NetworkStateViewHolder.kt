package com.aayush.filmipedia.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_network_state.view.*

class NetworkStateViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindView(networkState: NetworkState?) {
        if (networkState != null && networkState.status == NetworkState.Status.RUNNING) {
            itemView.progress_bar.visibility = View.VISIBLE
        } else {
            itemView.progress_bar.visibility = View.GONE
        }

        if (networkState != null && networkState.status == NetworkState.Status.FAILED) {
            itemView.text_error.visibility = View.VISIBLE
            itemView.text_error.text = networkState.msg
        } else {
            itemView.text_error.visibility = View.GONE
        }
    }
}