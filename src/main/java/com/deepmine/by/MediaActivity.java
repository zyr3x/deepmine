package com.deepmine.by;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.deepmine.by.adapters.ItemImageBinder;
import com.deepmine.by.components.TimerTaskPlus;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.services.DataService;
import com.deepmine.by.services.MediaService;
import com.google.analytics.tracking.android.EasyTracker;

import java.util.Timer;

public class MediaActivity extends Activity implements Constants {

    private ProgressDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_full);
        ResourceHelper.getInstance().init(this);
        EasyTracker.getInstance().activityStart(this); // Add this method.
        ListView listView = (ListView) findViewById(R.id.listNext);

        SimpleAdapter simpleAdapter = new SimpleAdapter( getApplicationContext(),
                DataService.getMediaPlaylist().getSimpleAdapterList(),
                R.layout.playlist_row,
                getResources().getStringArray(R.array.playlist_names),
                ResourceHelper.getInstance().getIntArray(R.array.playlist_ids

                )
        );

        simpleAdapter.setViewBinder(new ItemImageBinder());
        listView.setAdapter(simpleAdapter);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaService.setDataTitle(DataService.getMediaPlaylist().getItem(i));
                MediaService.play(MediaActivity.this);
                showLoading();
                checkStatus();
            }
        });
        simpleAdapter.notifyDataSetChanged();

    }

    protected void checkStatus()
    {
        new Timer().scheduleAtFixedRate(new TimerTaskPlus() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        updateStatus();
                    }
                });
            }
        }, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    private void updateStatus() {
        if (MediaService.isPlaying()) {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
            finish();
        }

        if ( MediaService.isErrors()) {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();

            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
            MediaService.cleanErrors();
            MediaService.stop();
        }
    }

    private void showLoading() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getText(R.string.connection_media));
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
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
