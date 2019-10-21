package com.burakocak.githubrepo.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.burakocak.githubrepo.model.Favorite;


@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFavorite(Favorite favorite);

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE repoId = :id)")
    Integer isFavorite(long id);

    @Query("DELETE FROM favorite WHERE repoId = :repoId")
    void deleteByFavoriteId(long repoId);
}
