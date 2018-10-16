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
import com.aayush.filmipedia.model.Person;
import com.aayush.filmipedia.model.PersonResult;
import com.aayush.filmipedia.util.NetworkState;
import com.aayush.filmipedia.view.activity.PersonDetailActivity;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

import static com.aayush.filmipedia.util.Constants.EXTRAS_TAG;

public class PersonAdapter extends PagedListAdapter<PersonResult, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private List<PersonResult> resultList;
    private Context context;
    private NetworkState networkState;

    private static final DiffUtil.ItemCallback<PersonResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<PersonResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull PersonResult oldItem, @NonNull PersonResult newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PersonResult oldItem,
                                          @NonNull PersonResult newItem) {
            return oldItem.equals(newItem);
        }
    };

    public PersonAdapter(Context context, List<PersonResult> resultList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PROGRESS) {
            View view = inflater.inflate(R.layout.item_network_state, parent, false);
            return new PersonAdapter.NetworkStateViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.card_movie_results, parent, false);
            return new PersonAdapter.PersonResultsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonAdapter.PersonResultsViewHolder) {
            ((PersonAdapter.PersonResultsViewHolder) holder).bindTo(getItem(position));
        }
        else {
            ((PersonAdapter.NetworkStateViewHolder) holder).bindView(networkState);
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

    class PersonResultsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_movie_title)
        TextView titleTextView;
        @BindView(R.id.img_movie_poster)
        ImageView thumbnailImageView;

        PersonResultsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(
                    v -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            PersonResult personResult = getItem(position);
                            FilmipediaApplication application = FilmipediaApplication.create(context);
                            Person person;

                            person = application.getRestApi()
                                    .getPersonById(Objects.requireNonNull(personResult).getId(),
                                            BuildConfig.THE_MOVIE_DB_API_KEY)
                                    .subscribeOn(Schedulers.io())
                                    .blockingFirst(null);

                            Intent intent = new Intent(context, PersonDetailActivity.class);
                            intent.putExtra(EXTRAS_TAG, person);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
            );
        }

        void bindTo(PersonResult result) {
            if (result != null) {
                titleTextView.setText(result.getName());
                GlideApp.with(context)
                        .load("https://image.tmdb.org/t/p/w500" +
                                result.getProfilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(thumbnailImageView);
            }
        }
    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) ProgressBar progressBar;
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
