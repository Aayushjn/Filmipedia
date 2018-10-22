package com.aayush.filmipedia.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MovieCreditsResponse {
    @Expose
    private Long id;

    @Expose
    private List<MovieCast> cast;

    @Expose
    private List<MovieCrew> crew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MovieCast> getCast() {
        return cast;
    }

    public void setCast(List<MovieCast> cast) {
        this.cast = cast;
    }

    public List<MovieCrew> getCrew() {
        return crew;
    }

    public void setCrew(List<MovieCrew> crew) {
        this.crew = crew;
    }
}
