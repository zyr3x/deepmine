package com.deepmine.by;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.deepmine.by.adapters.ItemImageBinder;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.services.DataService;
import com.google.analytics.tracking.android.EasyTracker;

public class NextActivity extends Activity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        ResourceHelper.getInstance().init(this);
        EasyTracker.getInstance().activityStart(this); // Add this method.

        ListView listView = (ListView) findViewById(R.id.listNext);

        SimpleAdapter simpleAdapter = new SimpleAdapter( getApplicationContext(),
                DataService.getPlaylist().getSimpleAdapterList(),
                R.layout.playlist_row,
                getResources().getStringArray(R.array.playlist_names),
                ResourceHelper.getInstance().getIntArray(R.array.playlist_ids)
        );

        simpleAdapter.setViewBinder(new ItemImageBinder());
        listView.setAdapter(simpleAdapter);
        listView.setDividerHeight(0);
        simpleAdapter.notifyDataSetChanged();

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