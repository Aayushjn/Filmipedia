
package com.aayush.filmipedia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

public class PersonResult implements Parcelable {
    @Expose
    private Double popularity;

    @Expose
    private Long id;

    @SerializedName("profile_path")
    private String profilePath;

    @Expose
    private String name;

    @SerializedName("known_for")
    private List<Movie> movies;

    @Expose
    private Boolean adult;

    private PersonResult(Parcel in) {
        if (in.readByte() == 0) {
            popularity = null;
        }
        else {
            popularity = in.readDouble();
        }
        if (in.readByte() == 0) {
            id = null;
        }
        else {
            id = in.readLong();
        }
        profilePath = in.readString();
        name = in.readString();
        movies = in.createTypedArrayList(Movie.CREATOR);
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
    }

    public static final Creator<PersonResult> CREATOR = new Creator<PersonResult>() {
        @Override
        public PersonResult createFromParcel(Parcel in) {
            return new PersonResult(in);
        }

        @Override
        public PersonResult[] newArray(int size) {
            return new PersonResult[size];
        }
    };

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (popularity == null) {
            dest.writeByte((byte) 0);
        }
        else {
            dest.writeByte((byte) 1);
            dest.writeDouble(popularity);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        }
        else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(profilePath);
        dest.writeString(name);
        dest.writeTypedList(movies);
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(popularity, id, profilePath, name, movies, adult);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PersonResult)) {
            return false;
        }

        PersonResult other = (PersonResult) obj;

        return Objects.equals(popularity, other.popularity) &&
                Objects.equals(id, other.id) &&
                Objects.equals(profilePath, other.profilePath) &&
                Objects.equals(name, other.name) &&
                Objects.equals(movies, other.movies) &&
                Objects.equals(adult, other.adult);
    }
}
