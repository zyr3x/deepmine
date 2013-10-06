package com.deepmine.by;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.deepmine.by.adapters.ItemImageBinder;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.models.DataTitle;
import com.deepmine.by.services.DataService;
import com.deepmine.by.services.MediaService;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence title = ((TextView)view.findViewById(R.id.title)).getText();
                CharSequence trackArtist = ((TextView)view.findViewById(R.id.trackArtist)).getText();
                CharSequence trackTitle = ((TextView)view.findViewById(R.id.trackTitle)).getText();
                DataTitle dataTitle = new DataTitle();
                dataTitle.title = title.toString();
                dataTitle.artist = trackArtist.toString();
                dataTitle.track = trackTitle.toString();
                MediaService.setDataTitle(dataTitle);
                MediaService.play();
            }
        });
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
