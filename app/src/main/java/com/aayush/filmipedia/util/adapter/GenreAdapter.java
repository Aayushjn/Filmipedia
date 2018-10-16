package com.aayush.filmipedia.util.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.Genre;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.view.fragment.MainFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class GenreAdapter extends ListAdapter<Genre, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private List<Genre> genreList;
    private AppCompatActivity activity;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<Genre> DIFF_CALLBACK = new DiffUtil.ItemCallback<Genre>() {
        @Override
        public boolean areItemsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.equals(newItem);
        }
    };

    public GenreAdapter(AppCompatActivity activity, List<Genre> genreList) {
        super(DIFF_CALLBACK);
        this.activity = activity;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new GenreAdapter.NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_genre, parent, false);
            return new GenreAdapter.GenreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GenreAdapter.GenreViewHolder) {
            ((GenreAdapter.GenreViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((GenreAdapter.NetworkStateViewHolder) holder).bindView(networkState);
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

    class GenreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_genre) TextView genreTextView;

        GenreViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Genre genre = getItem(position);
                            Fragment fragment = MainFragment.newInstance();
                            Bundle args = new Bundle();
                            args.putLong(EXTRAS_TAG, genre.getId());
                            fragment.setArguments(args);

                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frag_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
            );
        }

        void bindTo(Genre genre) {
            if (genre != null) {
                genreTextView.setText(genre.getName());
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
