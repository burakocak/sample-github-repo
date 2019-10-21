package com.burakocak.githubrepo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GitHubRepo implements Serializable {

    @SerializedName("id")
    private String idRep;

    private String name;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("open_issues_count")
    private Integer openIssuesCount;

    @SerializedName("forks_count")
    private Integer forksCount;

    @SerializedName("watchers_count")
    private Integer watchersCount;

    @SerializedName("language")
    private String language;


    public GitHubRepo() {
    }


    public String getId() {
        return idRep;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIdRep() {
        return idRep;
    }

    public Owner getOwner() {
        return owner;
    }

    public Integer getOpenIssuesCount() {
        return openIssuesCount;
    }

    public Integer getForksCount() {
        return forksCount;
    }

    public Integer getWatchersCount() {
        return watchersCount;
    }

    public String getLanguage() {
        return language;
    }

}
