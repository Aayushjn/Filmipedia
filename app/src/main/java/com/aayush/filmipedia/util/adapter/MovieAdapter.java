package com.aayush.filmipedia.util.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aayush.filmipedia.GlideApp;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.view.activity.DetailActivity;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class MovieAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private List<Movie> movieList;
    private Context context;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MovieAdapter(Context context, List<Movie> movieList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            ((MovieViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((NetworkStateViewHolder) holder).bindView(networkState);
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

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_movie_title)  TextView titleTextView;
        @BindView(R.id.text_movie_desc)   TextView descriptionTextView;
        @BindView(R.id.text_movie_rating) TextView userRatingTextView;
        @BindView(R.id.img_movie_poster)  ImageView thumbnailImageView;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Movie movie = getItem(position);
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(EXTRAS_TAG, movie);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
            );
        }

        void bindTo(Movie movie) {
            if (movie != null) {
                titleTextView.setText(movie.getOriginalTitle());
                userRatingTextView.setText(String.format(Locale.getDefault(), "%s",
                        movie.getVoteAverage()));
                descriptionTextView.setText(movie.getOverview());
                GlideApp.with(context)
                        .load("https://image.tmdb.org/t/p/w500" +
                                movie.getPosterPath())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(thumbnailImageView);
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
