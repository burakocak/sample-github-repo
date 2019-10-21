package com.burakocak.githubrepo.viewmodel;

import android.app.Application;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.burakocak.githubrepo.database.FavoriteDatabase;
import com.burakocak.githubrepo.database.dao.FavoriteDao;
import com.burakocak.githubrepo.model.EventBusObject;
import com.burakocak.githubrepo.model.Favorite;
import com.burakocak.githubrepo.model.GitHubRepo;
import com.burakocak.githubrepo.service.RestApiBuilder;
import com.burakocak.githubrepo.service.RestApiService;
import com.burakocak.githubrepo.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> allFavorite;
    private ExecutorService executorService;
    public MutableLiveData<List<GitHubRepo>> userRepos = new MutableLiveData<>();
    public MutableLiveData<List<Favorite>> favoriteList = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getDatabase(application);
        favoriteDao = favoriteDatabase.favoriteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void fetchRepos(Editable text) {
        if (text.toString().isEmpty()) {
            userRepos.setValue(null);
            EventBus.getDefault().post(new EventBusObject(Constants.RESULT_NO));
            return;
        }
        EventBus.getDefault().post(new EventBusObject(Constants.SHOW_LOADING));
        RestApiService apiService = new RestApiBuilder().getService();
        Call<List<GitHubRepo>> repoListCall = apiService.getUserRepos(text.toString());
        repoListCall.enqueue(new Callback<List<GitHubRepo>>() {

            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    userRepos.setValue(response.body());
                    EventBus.getDefault().post(new EventBusObject(Constants.HIDE_LOADING));

                } else {
                    userRepos.setValue(null);
                    EventBus.getDefault().post(new EventBusObject(Constants.HIDE_LOADING));
                    EventBus.getDefault().post(new EventBusObject(Constants.RESULT_NO));
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                EventBus.getDefault().post(new EventBusObject(Constants.HIDE_LOADING));
            }
        });
    }


    public void deleteFavorite(long id) {
        executorService.execute(() -> favoriteDao.deleteByFavoriteId(id));
    }

    public void insertFavorite(Favorite list) {
        executorService.execute(() -> favoriteDao.insertFavorite(list));
    }

    public int isFavoriteItem(long id) {
        int result = 0;
        try {
            result = executorService.submit(() -> favoriteDao.isFavorite(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<List<Favorite>> getAllFavorite() {
        return allFavorite;
    }

    public void setAllFavorite(LiveData<List<Favorite>> allFavorite) {
        this.allFavorite = allFavorite;
    }


}