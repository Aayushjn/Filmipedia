package com.aayush.filmipedia.util.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aayush.filmipedia.BuildConfig;
import com.aayush.filmipedia.FilmipediaApplication;
import com.aayush.filmipedia.GlideApp;
import com.aayush.filmipedia.R;
import com.aayush.filmipedia.model.MovieCrew;
import com.aayush.filmipedia.model.Person;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.util.Utility;
import com.aayush.filmipedia.view.activity.PersonDetailActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class MovieCrewAdapter extends ListAdapter<MovieCrew, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<MovieCrew> trailerList;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<MovieCrew> DIFF_CALLBACK = new DiffUtil.ItemCallback<MovieCrew>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieCrew oldItem, @NonNull MovieCrew newItem) {
            return oldItem.getCreditId().equals(newItem.getCreditId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieCrew oldItem, @NonNull MovieCrew newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MovieCrewAdapter(Context context, List<MovieCrew> trailers) {
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
            return new MovieCrewAdapter.NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_known_for, parent, false);
            return new MovieCrewAdapter.MovieCrewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieCrewAdapter.MovieCrewViewHolder) {
            ((MovieCrewAdapter.MovieCrewViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((MovieCrewAdapter.NetworkStateViewHolder) holder).bindView(networkState);
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

    class MovieCrewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_movie_poster)
        ImageView posterImageView;
        @BindView(R.id.text_character)
        TextView characterTextView;
        @BindView(R.id.text_movie_title) TextView titleTextView;

        MovieCrewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (Utility.isNetworkNotAvailable(context)) {
                                Snackbar.make(view, "Connect to internet to view details!",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                            else {
                                MovieCrew movieCrew = getItem(position);
                                FilmipediaApplication application = FilmipediaApplication
                                        .create(context);
                                Person person;

                                person = application.getRestApi()
                                        .getPersonById(Objects.requireNonNull(movieCrew).getId(),
                                                BuildConfig.THE_MOVIE_DB_API_KEY)
                                        .subscribeOn(Schedulers.io())
                                        .blockingFirst(null);

                                Intent intent = new Intent(context, PersonDetailActivity.class);
                                intent.putExtra(EXTRAS_TAG, person);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }

                    }
            );
        }

        void bindTo(MovieCrew movieCrew) {
            if (movieCrew != null) {
                characterTextView.setText(movieCrew.getJob());
                titleTextView.setText(movieCrew.getName());
                GlideApp.with(context)
                        .load("https://image.tmdb.org/t/p/w500" +
                                movieCrew.getProfilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(posterImageView);
            }
        }
    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
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
