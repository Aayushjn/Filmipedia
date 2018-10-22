package com.aayush.filmipedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MovieCrew {
    @SerializedName("credit_id")
    private String creditId;

    @Expose
    private String department;

    @Expose
    private Long gender;

    @Expose
    private Long id;

    @Expose
    private String job;

    @Expose
    private String name;

    @SerializedName("profile_path")
    private Object profilePath;

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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(Object profilePath) {
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

        MovieCrew movieCrew = (MovieCrew) o;
        return Objects.equals(creditId, movieCrew.creditId) &&
                Objects.equals(department, movieCrew.department) &&
                Objects.equals(gender, movieCrew.gender) &&
                Objects.equals(id, movieCrew.id) &&
                Objects.equals(job, movieCrew.job) &&
                Objects.equals(name, movieCrew.name) &&
                Objects.equals(profilePath, movieCrew.profilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditId, department, gender, id, job, name, profilePath);
    }
}
