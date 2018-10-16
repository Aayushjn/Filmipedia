
package com.aayush.filmipedia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person implements Parcelable {
    @Expose
    private Boolean adult;

    @SerializedName("also_known_as")
    private List<String> alsoKnownAs;

    @Expose
    private String biography;

    @Expose
    private String birthday;

    @Expose
    private String deathday;

    @Expose
    private int gender;

    @Expose
    private Object homepage;

    @Expose
    private Long id;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("known_for_department")
    private String knownForDepartment;

    @Expose
    private String name;

    @SerializedName("place_of_birth")
    private String placeOfBirth;

    @Expose
    private Double popularity;

    @SerializedName("profile_path")
    private String profilePath;

    private Person(Parcel in) {
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        alsoKnownAs = in.createStringArrayList();
        biography = in.readString();
        birthday = in.readString();
        deathday = in.readString();
        gender = in.readInt();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        imdbId = in.readString();
        knownForDepartment = in.readString();
        name = in.readString();
        placeOfBirth = in.readString();
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readDouble();
        }
        profilePath = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public List<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Object getHomepage() {
        return homepage;
    }

    public void setHomepage(Object homepage) {
        this.homepage = homepage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
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
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeStringList(alsoKnownAs);
        dest.writeString(biography);
        dest.writeString(birthday);
        dest.writeString(deathday);
        dest.writeInt(gender);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(imdbId);
        dest.writeString(knownForDepartment);
        dest.writeString(name);
        dest.writeString(placeOfBirth);
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(popularity);
        }
        dest.writeString(profilePath);
    }
}
