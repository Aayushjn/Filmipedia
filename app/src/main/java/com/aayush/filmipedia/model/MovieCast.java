package com.aayush.filmipedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MovieCast {
    @SerializedName("cast_id")
    private Long castId;

    @Expose
    private String character;

    @SerializedName("credit_id")
    private String creditId;

    @Expose
    private Long gender;

    @Expose
    private Long id;

    @Expose
    private String name;

    @Expose
    private Long order;

    @SerializedName("profile_path")
    private String profilePath;

    public Long getCastId() {
        return castId;
    }

    public void setCastId(Long castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieCast movieCast = (MovieCast) o;

        return Objects.equals(castId, movieCast.castId) &&
                Objects.equals(character, movieCast.character) &&
                Objects.equals(creditId, movieCast.creditId) &&
                Objects.equals(gender, movieCast.gender) &&
                Objects.equals(id, movieCast.id) &&
                Objects.equals(name, movieCast.name) &&
                Objects.equals(order, movieCast.order) &&
                Objects.equals(profilePath, movieCast.profilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(castId, character, creditId, gender, id, name, order, profilePath);
    }
}
