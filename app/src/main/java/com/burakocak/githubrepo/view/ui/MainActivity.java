package com.burakocak.githubrepo.view.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.burakocak.githubrepo.R;
import com.burakocak.githubrepo.databinding.ActivityMainBinding;
import com.burakocak.githubrepo.model.EventBusObject;
import com.burakocak.githubrepo.model.Favorite;
import com.burakocak.githubrepo.model.GitHubRepo;
import com.burakocak.githubrepo.utils.Constants;
import com.burakocak.githubrepo.view.adapter.RepoListAdapter;
import com.burakocak.githubrepo.view.base.BaseActivity;
import com.burakocak.githubrepo.view.callback.OnRecyclerItemClickListener;
import com.burakocak.githubrepo.viewmodel.MainViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity implements OnRecyclerItemClickListener{

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private RepoListAdapter repoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);

        init();

        viewModel.userRepos.observe(this, new Observer<List<GitHubRepo>>() {
            @Override
            public void onChanged(List<GitHubRepo> gitHubRepos) {
                repoListAdapter.setUserRepos(gitHubRepos);
            }
        });

    }

    private void init(){
        binding.rvRepos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRepos.setHasFixedSize(true);
        repoListAdapter = new RepoListAdapter(this,this ,viewModel);
        binding.rvRepos.setAdapter(repoListAdapter);

        binding.btnSearch.setOnClickListener(view -> viewModel.fetchRepos(Objects.requireNonNull(binding.etSearch.getText())));
    }

    @Override
    public void onCustomEvent(EventBusObject eventbusObject) {
        if (eventbusObject.getKey() == Constants.RESULT_NO) {
            runOnUiThread(() -> showErrorSneaker("User Error!", "don't match github username"));
        }
        if (eventbusObject.getKey()== Constants.UPDATE_FAVORITE) {
            repoListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onFavoriteClick(Object object) {
        GitHubRepo gitHubRepo = (GitHubRepo) object;

        long favoriteId = Long.parseLong(gitHubRepo.getId());

        int favoriteResult = viewModel.isFavoriteItem(favoriteId);
        if (favoriteResult == 1) {
            viewModel.deleteFavorite(favoriteId);
            repoListAdapter.notifyDataSetChanged();
            showSuccessSneaker("Favorite!!", "Remove your from favorite");
        } else {
            Favorite favorite = new Favorite();
            favorite.setUsername(gitHubRepo.getName());
            favorite.setId(Long.parseLong(gitHubRepo.getId()));
            favorite.setFavorite(true);
            viewModel.insertFavorite(favorite);
            repoListAdapter.notifyDataSetChanged();
            showSuccessSneaker("Favorite!!", "Added your from favorite");
        }
    }

    @Override
    public void onDetailClick(Object object, boolean state) {

        GitHubRepo gitHubRepo = (GitHubRepo) object;
        Intent intent = new Intent(MainActivity.this,RepoDetailsActivity.class);
        intent.putExtra("Data", gitHubRepo);
        intent.putExtra("state",state);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        showExitApplicationDialog("Exit Application!!","Do you really want to Exit?");
    }
}
