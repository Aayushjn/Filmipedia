package com.aayush.filmipedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import androidx.annotation.Nullable;

public class Trailer {
    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    public Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Trailer)) {
            return false;
        }

        Trailer other = (Trailer) obj;

        return Objects.equals(key, other.key) &&
                Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, name);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
