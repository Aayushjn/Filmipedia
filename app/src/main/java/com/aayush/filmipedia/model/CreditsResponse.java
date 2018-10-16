
package com.aayush.filmipedia.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CreditsResponse {
    @Expose
    private List<Cast> cast;

    @Expose
    private List<Crew> crew;

    @Expose
    private Long id;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
