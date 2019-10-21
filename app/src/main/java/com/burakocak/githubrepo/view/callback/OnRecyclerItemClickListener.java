package com.burakocak.githubrepo.view.callback;


public interface OnRecyclerItemClickListener {

    /*
        TThis listener was created for the click operations
        for the buttons on the recyclerView.

    */

    void onFavoriteClick(Object object);
    void onDetailClick(Object object,boolean state);
}
