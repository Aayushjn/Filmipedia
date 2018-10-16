package com.aayush.filmipedia.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Trailer;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends ListAdapter<Trailer, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Trailer> trailerList;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<Trailer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Trailer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Trailer oldItem, @NonNull Trailer newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(DIFF_CALLBACK);

        this.context = context;
        trailerList = trailers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new TrailerAdapter.NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_trailer, parent, false);
            return new TrailerAdapter.TrailerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TrailerAdapter.TrailerViewHolder) {
            ((TrailerAdapter.TrailerViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((TrailerAdapter.NetworkStateViewHolder) holder).bindView(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = networkState;
        boolean previousExtraRow = hasExtraRow();
        networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            }
            else {
                notifyItemInserted(getItemCount());
            }
        }
        else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_trailer_title) TextView titleTextView;

        TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Utility.watchYoutubeVideo(getItem(position).getKey(),
                                    context);
                        }
                    }
            );
        }

        void bindTo(Trailer trailer) {
            if (trailer != null) {
                titleTextView.setText(trailer.getName());
            }
        }
    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.text_error)    TextView errorMessageTextView;

        NetworkStateViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            }
            else {
                progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                errorMessageTextView.setVisibility(View.VISIBLE);
                errorMessageTextView.setText(networkState.getMsg());
            }
            else {
                errorMessageTextView.setVisibility(View.GONE);
            }
        }
    }
}
