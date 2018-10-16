package com.aayush.filmipedia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Movie implements Parcelable {
    @SerializedName("id")
    private Long mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("vote_average")
    private Double mVoteAverage;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("adult")
    private boolean mAdult;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("genre_ids")
    private List<Long> mGenreIds;

    @SerializedName("original_title")
    private String mOriginalTitle;

    @SerializedName("original_language")
    private String mOriginalLanguage;

    @SerializedName("backdrop_path")
    private String mBackdropPath;

    @SerializedName("popularity")
    private Double mPopularity;

    @SerializedName("vote_count")
    private Long mVoteCount;

    @SerializedName("video")
    private boolean mVideo;

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        mPosterPath = in.readString();
        mAdult = (boolean) in.readValue(getClass().getClassLoader());
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mGenreIds = new ArrayList<>();
        in.readList(mGenreIds, Integer.class.getClassLoader());
        mId = in.readLong();
        mOriginalTitle = in.readString();
        mOriginalLanguage = in.readString();
        mTitle = in.readString();
        mBackdropPath = in.readString();
        mPopularity = in.readDouble();
        mVoteCount = in.readLong();
        mVideo = (boolean) in.readValue(getClass().getClassLoader());
        mVoteAverage = in.readDouble();
    }

    public Movie(Cast cast) {
        this.mPosterPath = cast.getPosterPath();
        this.mAdult = cast.getAdult();
        this.mOverview = cast.getOverview();
        this.mReleaseDate = cast.getReleaseDate();
        this.mGenreIds = cast.getGenreIds();
        this.mId = cast.getId();
        this.mOriginalTitle = cast.getOriginalTitle();
        this.mOriginalLanguage = cast.getOriginalLanguage();
        this.mTitle = cast.getTitle();
        this.mBackdropPath = cast.getBackdropPath();
        this.mPopularity = cast.getPopularity();
        this.mVoteCount = cast.getVoteCount();
        this.mVideo = cast.getVideo();
        this.mVoteAverage = cast.getVoteAverage();
    }

    public Movie(Crew crew) {
        this.mPosterPath = crew.getPosterPath();
        this.mAdult = crew.getAdult();
        this.mOverview = crew.getOverview();
        this.mReleaseDate = crew.getReleaseDate();
        this.mGenreIds = crew.getGenreIds();
        this.mId = crew.getId();
        this.mOriginalTitle = crew.getOriginalTitle();
        this.mOriginalLanguage = crew.getOriginalLanguage();
        this.mTitle = crew.getTitle();
        this.mBackdropPath = crew.getBackdropPath();
        this.mPopularity = crew.getPopularity();
        this.mVoteCount = crew.getVoteCount();
        this.mVideo = crew.getVideo();
        this.mVoteAverage = crew.getVoteAverage();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeValue(mAdult);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeList(mGenreIds);
        dest.writeLong(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mTitle);
        dest.writeString(mBackdropPath);
        dest.writeDouble(mPopularity);
        dest.writeLong(mVoteCount);
        dest.writeValue(mVideo);
        dest.writeDouble(mVoteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Movie)) {
            return false;
        }

        Movie other = (Movie) obj;

        return Objects.equals(mPosterPath, other.mPosterPath) &&
                Objects.equals(mAdult, other.mAdult) &&
                Objects.equals(mOverview, other.mOverview) &&
                Objects.equals(mReleaseDate, other.mReleaseDate) &&
                Objects.equals(mGenreIds, other.getGenreIds()) &&
                Objects.equals(mId, other.mId) &&
                Objects.equals(mOriginalTitle, other.mOriginalTitle) &&
                Objects.equals(mOriginalLanguage, other.mOriginalLanguage) &&
                Objects.equals(mTitle, other.mTitle) &&
                Objects.equals(mBackdropPath, other.mBackdropPath) &&
                Objects.equals(mPopularity, other.mPopularity) &&
                Objects.equals(mVoteCount, other.mVoteCount) &&
                Objects.equals(mVideo, other.mVideo) &&
                Objects.equals(mVoteAverage, other.mVoteAverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPosterPath, mAdult, mOverview, mReleaseDate, mGenreIds, mId,
                mOriginalTitle, mOriginalLanguage, mTitle, mBackdropPath, mPopularity, mVoteCount,
                mVideo, mVoteAverage);
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public List<Long> getGenreIds() {
        return mGenreIds;
    }

    public Long getId() {
        return mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public Long getVoteCount() {
        return mVoteCount;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }
}
