package com.deepmine.by.components;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.deepmine.by.R;
import com.deepmine.by.helpers.Constants;
import com.google.analytics.tracking.android.EasyTracker;

import java.util.Timer;

/**
 * Created by Гость on 09.10.13.
 */
public abstract class BaseActivity extends Activity implements Constants {

    protected ProgressDialog loadingDialog = null;
    protected Timer timer;
    public void showLoading() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getText(R.string.connection_media));
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    public void hideLoading()
    {
        loadingDialog.dismiss();
    }

    public boolean statusLoading()
    {
        if (loadingDialog != null && loadingDialog.isShowing())
            return true;
        else
            return false;
    }

    public void showErrorToast()
    {
        Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
    }


    public void checkStatus(final Runnable runnable)
    {
         timer = new Timer();
         timer.scheduleAtFixedRate(new TimerTaskPlus() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    public void stopCheckStatus()
    {
        timer.cancel();
    }

    @Override
    public void onStart() {
        EasyTracker.getInstance().activityStart(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EasyTracker.getInstance().activityStop(this);
        super.onStop();
    }
}
