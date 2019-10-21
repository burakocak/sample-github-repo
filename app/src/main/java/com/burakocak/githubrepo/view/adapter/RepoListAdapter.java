package com.burakocak.githubrepo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burakocak.githubrepo.R;
import com.burakocak.githubrepo.model.GitHubRepo;
import com.burakocak.githubrepo.view.callback.OnRecyclerItemClickListener;
import com.burakocak.githubrepo.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoListHolder> {
    private List<GitHubRepo> gitHubRepoList;
    private LayoutInflater layoutInflater;
    private MainViewModel mainViewModel;
    private Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public RepoListAdapter(Context context,OnRecyclerItemClickListener listener, MainViewModel viewModel) {
        gitHubRepoList = new ArrayList<>();
        this.mainViewModel = viewModel;
        this.context = context;
        this.onRecyclerItemClickListener = listener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RepoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.rv_repo_item, parent, false);
        return new RepoListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoListHolder holder, int position) {
        GitHubRepo gitHubRepoCurrent = gitHubRepoList.get(position);
        holder.isFavorite = mainViewModel.isFavoriteItem(Long.parseLong(gitHubRepoList.get(position).getId()));
        holder.tvRepoTitle.setText(gitHubRepoCurrent.getName());

        holder.ivFavorite.setOnClickListener(v -> {
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.onFavoriteClick(gitHubRepoList.get(position));
            }
        });

        holder.tvRepoTitle.setOnClickListener(v -> {
            if (onRecyclerItemClickListener != null) {
                boolean state = false;
                if(holder.isFavorite == 1)  state = true;
                onRecyclerItemClickListener.onDetailClick(gitHubRepoList.get(position),state);
            }
        });



        if (holder.isFavorite == 1) {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public int getItemCount() {
        if (gitHubRepoList == null) return 0;
        return gitHubRepoList.size();
    }

    public void setUserRepos(List<GitHubRepo> repos) {
        this.gitHubRepoList = repos;
        notifyDataSetChanged();
    }


    public class RepoListHolder extends RecyclerView.ViewHolder {
        private TextView tvRepoTitle;
        private ImageView ivFavorite;
        private int isFavorite;

        private RepoListHolder(@NonNull View itemView) {
            super(itemView);
            tvRepoTitle = itemView.findViewById(R.id.repoTitle);
            ivFavorite = itemView.findViewById(R.id.repoFavoriteIcon);
        }
    }
}
