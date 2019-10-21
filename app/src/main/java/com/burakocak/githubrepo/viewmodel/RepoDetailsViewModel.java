package com.burakocak.githubrepo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.burakocak.githubrepo.database.FavoriteDatabase;
import com.burakocak.githubrepo.database.dao.FavoriteDao;
import com.burakocak.githubrepo.model.Favorite;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepoDetailsViewModel extends AndroidViewModel {

    private ExecutorService executorService;
    private FavoriteDao favoriteDao;

    public RepoDetailsViewModel(@NonNull Application application) {
        super(application);
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getDatabase(application);
        favoriteDao = favoriteDatabase.favoriteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertFavorite(Favorite list) {
        executorService.execute(() -> favoriteDao.insertFavorite(list));
    }

    public void deleteFavorite(long id) {
        executorService.execute(() -> favoriteDao.deleteByFavoriteId(id));
    }
}
