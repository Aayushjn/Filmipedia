
package com.aayush.filmipedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Crew {
    @Expose
    private Boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("credit_id")
    private String creditId;

    @Expose
    private String department;

    @SerializedName("genre_ids")
    private List<Long> genreIds;

    @Expose
    private Long id;

    @Expose
    private String job;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @Expose
    private String overview;

    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @Expose
    private String title;

    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Long voteCount;

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adult, backdropPath, creditId, department, genreIds, id, job,
                originalLanguage, originalTitle, overview, popularity, posterPath, releaseDate,
                title, video, voteAverage, voteCount);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Crew)) {
            return false;
        }

        Crew other = (Crew) obj;

        return Objects.equals(adult, other.adult) &&
                Objects.equals(backdropPath, other.backdropPath) &&
                Objects.equals(creditId, other.creditId) &&
                Objects.equals(department, other.department) &&
                Objects.equals(genreIds, other.genreIds) &&
                Objects.equals(id, other.id) &&
                Objects.equals(job, other.job) &&
                Objects.equals(originalLanguage, other.originalLanguage) &&
                Objects.equals(originalTitle, other.originalTitle) &&
                Objects.equals(overview, other.overview) &&
                Objects.equals(popularity, other.popularity) &&
                Objects.equals(posterPath, other.posterPath) &&
                Objects.equals(releaseDate, other.releaseDate) &&
                Objects.equals(title, other.title) &&
                Objects.equals(video, other.video) &&
                Objects.equals(voteAverage, other.voteAverage) &&
                Objects.equals(voteCount, other.voteCount);
    }
}
