package com.deepmine.by;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deepmine.by.adapters.SimpleAdaptersPlus;
import com.deepmine.by.adapters.ViewBinderPlus;
import com.deepmine.by.components.BaseActivity;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.services.DataService;
import com.deepmine.by.services.MediaService;

public class MediaActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_full);
        listView = (ListView) findViewById(R.id.listNext);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
        updateList(0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        MediaService.setDataTitle(DataService.getMediaPlaylist().getItem((int) id));
        DataService.getMediaPlaylist().setActive(DataService.getMediaPlaylist().getItem((int) id));
        MediaService.play(MediaActivity.this);
        showLoading();
        checkStatus(runnable);
        updateList((int)id);

    }

    protected void updateList(int pos) {
        SimpleAdaptersPlus simpleAdapter = new SimpleAdaptersPlus(getApplicationContext(),
                DataService.getMediaPlaylist().getSimpleAdapterList(),
                R.layout.playlist_row,
                getResources().getStringArray(R.array.playlist_names),
                ResourceHelper.getInstance().getIntArray(R.array.playlist_ids

                )
        );
        simpleAdapter.setViewBinder(new ViewBinderPlus());
        listView.setAdapter(simpleAdapter);

        if(pos!=0)
            listView.setSelection(pos);
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
