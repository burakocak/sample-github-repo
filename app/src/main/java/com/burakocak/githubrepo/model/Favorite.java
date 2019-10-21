package com.burakocak.githubrepo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "favorite")
public class Favorite {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "repoId")
    private long id;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "isCompleted")
    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public long getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setUsername(@NonNull String username) {
        this.username = username;
    }


}
