package com.burakocak.githubrepo.view.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.burakocak.githubrepo.R;
import com.burakocak.githubrepo.databinding.ActivityRepoDetailsBinding;
import com.burakocak.githubrepo.model.EventBusObject;
import com.burakocak.githubrepo.model.Favorite;
import com.burakocak.githubrepo.model.GitHubRepo;
import com.burakocak.githubrepo.view.base.BaseActivity;
import com.burakocak.githubrepo.viewmodel.RepoDetailsViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import static com.burakocak.githubrepo.utils.Constants.UPDATE_FAVORITE;

public class RepoDetailsActivity extends BaseActivity {

    private ActivityRepoDetailsBinding binding;
    private RepoDetailsViewModel viewModel;
    private GitHubRepo gitHubRepo;
    private boolean favoriteState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RepoDetailsActivity.this, R.layout.activity_repo_details);
        gitHubRepo = (GitHubRepo) getIntent().getSerializableExtra("Data");
        favoriteState = getIntent().getBooleanExtra("state", false);
        viewModel = ViewModelProviders.of(RepoDetailsActivity.this).get(RepoDetailsViewModel.class);
        assert getSupportActionBar() != null;
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init() {

        showLoading();

        Picasso.get().load(gitHubRepo.getOwner().avatarUrl).fit().into(binding.ivProfileImage,
        new Callback() {
            @Override
            public void onSuccess() {
                hideLoading();
            }

            @Override
            public void onError(Exception e) {
                hideLoading();

            }
        });
        binding.tvRepoName.setText(gitHubRepo.getName());
        binding.tvOwnerName.setText(gitHubRepo.getOwner().login);
        binding.tvProjectFullName.setText(gitHubRepo.getFullName());
        binding.tvLanguageName.setText(gitHubRepo.getLanguage());
        binding.tvForkCount.setText(String.valueOf(gitHubRepo.getForksCount()));
        binding.tvStartCount.setText(String.valueOf(gitHubRepo.getWatchersCount()));
        binding.tvIssueCount.setText(String.valueOf(gitHubRepo.getOpenIssuesCount()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_favorite, menu);
        if (!favoriteState) {
            menu.findItem(R.id.add_favorite_item).setIcon(R.drawable.ic_favorite_border);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_favorite_item) {
            Favorite favorite = new Favorite();
            if(favoriteState) {
                favoriteState = false;
                viewModel.deleteFavorite(Long.parseLong(gitHubRepo.getId()));
                item.setIcon(R.drawable.ic_favorite_border);
                showSuccessSneaker("Favorite!!", "Remove your from favorite");
                EventBus.getDefault().post(new EventBusObject(UPDATE_FAVORITE));
            }else {
                favoriteState = true;
                favorite.setFavorite(true);
                favorite.setUsername(gitHubRepo.getOwner().login);
                favorite.setId(Long.parseLong(gitHubRepo.getId()));
                viewModel.insertFavorite(favorite);
                item.setIcon(R.drawable.ic_favorite);
                showSuccessSneaker("Favorite!!", "Added your from favorite");
                EventBus.getDefault().post(new EventBusObject(UPDATE_FAVORITE));
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCustomEvent(EventBusObject eventbusObject) {

    }
}
