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
import com.aayush.filmipedia.model.Crew;
import com.aayush.filmipedia.model.Movie;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.view.activity.DetailActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class CrewAdapter extends ListAdapter<Crew, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Crew> crewList;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<Crew> DIFF_CALLBACK = new DiffUtil.ItemCallback<Crew>() {
        @Override
        public boolean areItemsTheSame(@NonNull Crew oldItem, @NonNull Crew newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Crew oldItem, @NonNull Crew newItem) {
            return oldItem.equals(newItem);
        }
    };

    public CrewAdapter(Context context, List<Crew> crewList) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new CrewAdapter.NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_known_for, parent, false);
            return new CrewAdapter.CrewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CrewAdapter.CrewViewHolder) {
            ((CrewAdapter.CrewViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((CrewAdapter.NetworkStateViewHolder) holder).bindView(networkState);
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

    class CrewViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.img_movie_poster) ImageView posterImageView;
        @BindView(R.id.text_movie_title) TextView titleTextView;
        @BindView(R.id.text_character)   TextView characterTextView;

        CrewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.view = view;

            view.setOnClickListener(
                    v -> {
                        Movie movie = new Movie(getItem(getAdapterPosition()));

                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(EXTRAS_TAG, movie);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
            );
        }

        void bindTo(Crew crew) {
            if (crew != null) {
                titleTextView.setText(crew.getTitle());
                characterTextView.setText(crew.getJob());
                GlideApp.with(view)
                        .load("https://image.tmdb.org/t/p/w500" + crew.getPosterPath())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(posterImageView);
            }
        }
    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.text_error)   TextView errorMessageTextView;

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
