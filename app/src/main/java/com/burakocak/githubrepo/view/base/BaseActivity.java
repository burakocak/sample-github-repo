package com.burakocak.githubrepo.view.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.burakocak.githubrepo.model.EventBusObject;
import com.burakocak.githubrepo.utils.Constants;
import com.burakocak.githubrepo.utils.Utils;
import com.irozon.sneaker.Sneaker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract void onCustomEvent(EventBusObject eventbusObject);

    private ProgressDialog mProgressDialog;

    public void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    public void showLoading(){
        hideKeyboard();
        mProgressDialog = Utils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog.isShowing() && mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }


    public void showErrorSneaker(String title , String message) {
        Sneaker.with(this)
                .setTitle(title)
                .setMessage(message)
                .sneakError();
    }

    public void showSuccessSneaker(String title , String message) {
        Sneaker.with(this)
                .setTitle(title)
                .setMessage(message)
                .sneakSuccess();
    }


    public void showExitApplicationDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Subscribe
    public void onEvent(Object object) {
        EventBusObject eventbusObject = (EventBusObject) object;
        if (eventbusObject.getKey() == Constants.SHOW_LOADING) {
            showLoading();
        } else if (eventbusObject.getKey() == Constants.HIDE_LOADING) {
            hideLoading();
        }
        onCustomEvent(eventbusObject);
    }


}
