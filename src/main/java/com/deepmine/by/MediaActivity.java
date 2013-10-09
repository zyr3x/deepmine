package com.deepmine.by;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deepmine.by.adapters.SimpleAdaptersPlus;
import com.deepmine.by.adapters.ViewBinderPlus;
import com.deepmine.by.components.BaseActivity;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.services.DataService;
import com.deepmine.by.services.MediaService;

public class MediaActivity extends BaseActivity implements Constants {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_full);
        listView = (ListView) findViewById(R.id.listNext);
        updateList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaService.setDataTitle(DataService.getMediaPlaylist().getItem((int) l));
                DataService.getMediaPlaylist().setActive(DataService.getMediaPlaylist().getItem((int) l));
                MediaService.play(MediaActivity.this);
                showLoading();
                checkStatus(runnable);
                updateList();
                listView.setSelection((int) l);
            }
        });

    }

    protected void updateList() {
        SimpleAdaptersPlus simpleAdapter = new SimpleAdaptersPlus(getApplicationContext(),
                DataService.getMediaPlaylist().getSimpleAdapterList(),
                R.layout.playlist_row,
                getResources().getStringArray(R.array.playlist_names),
                ResourceHelper.getInstance().getIntArray(R.array.playlist_ids

                )
        );
        simpleAdapter.setViewBinder(new ViewBinderPlus());
        listView.setAdapter(simpleAdapter);
        listView.setDividerHeight(0);

    }


    Runnable runnable = new Runnable() {
        public void run() {
            updateStatus();
        }
    };

    private void updateStatus() {
        if (MediaService.isPlaying()) {
            if (statusLoading())
                hideLoading();
        }

        if (MediaService.isErrors()) {
            if (statusLoading())
                hideLoading();

            showErrorToast();
            MediaService.cleanErrors();
            MediaService.stop();
        }
    }
}
