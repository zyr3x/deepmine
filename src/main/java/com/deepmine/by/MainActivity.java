package com.deepmine.by;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.Timer;

import com.deepmine.by.adapters.ViewBinderPlus;
import com.deepmine.by.components.BaseActivity;
import com.deepmine.by.components.TimerTaskPlus;
import com.deepmine.by.helpers.GSONTransformer;
import com.deepmine.by.helpers.ImageThreadLoader;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.models.Blocks;
import com.deepmine.by.services.DataService;
import com.deepmine.by.services.MediaService;
import com.deepmine.by.services.RadioService;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends BaseActivity {

    public static String TAG = MAIN_TAG + ":MainActivity";

    private ImageView mPlayBtn;
    private ListView mListView;
    public static TextView mTrackArtist;
    public static TextView mTrack;
    public static ImageView mCover;
    private Intent _radioService;
    private Intent _dataService;
    private Intent _mediaService;
    private String _lastCover = "";
    private AQuery _aQuery = new AQuery(this);
    private ImageThreadLoader imageThreadLoader = ImageThreadLoader.getOnDiskInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServices();
        init();
        checkStatus(runnable);
        getEvents();

    }

    protected void init()
    {
        ResourceHelper.getInstance().init(this);
        mTrackArtist = (TextView) findViewById(R.id.artist);
        mTrack = (TextView) findViewById(R.id.track);
        mPlayBtn = (ImageView) findViewById(R.id.playBtn);
        mCover = (ImageView) findViewById(R.id.trackCover);
        mListView = (ListView) findViewById(R.id.listEvents);
    }


    @Override
    public void onDestroy() {
        RadioService.stop();
        MediaService.stop();
        stopService(_radioService);
        stopService(_dataService);
        stopService(_mediaService);
        super.onDestroy();
    }

    protected void startServices() {
        if (!DataService.status()) {
            _dataService = new Intent(getApplicationContext(), DataService.class);
            _mediaService = new Intent(getApplicationContext(), MediaService.class);
            _radioService = new Intent(getApplicationContext(), RadioService.class);
            startService(_dataService);
            startService(_mediaService);
        }
    }

    public void getEvents() {
        _aQuery.transformer(new GSONTransformer())
                .ajax(
                        EVENT_URL,
                        Blocks.class,
                        new AjaxCallback<Blocks>() {
                            public void callback(String url, Blocks blocks, AjaxStatus status) {

                                SimpleAdapter simpleAdapter = new SimpleAdapter
                                        (getApplicationContext(),
                                                blocks.getList(),
                                                R.layout.menu_row,
                                                getResources().getStringArray(R.array.menu_row_element_names),
                                                ResourceHelper.getInstance().getIntArray(R.array.menu_row_element_ids)
                                        );

                                simpleAdapter.setViewBinder(new ViewBinderPlus());

                                mListView.setAdapter(simpleAdapter);
                                mListView.setDividerHeight(0);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view,
                                                            int i, long l) {
                                        Intent browserIntent = new Intent(
                                                Intent.ACTION_VIEW, Uri.parse(((TextView) view
                                                .findViewById(R.id.link)).getText().toString()));
                                        startActivity(browserIntent);


                                    }
                                });
                                simpleAdapter.notifyDataSetChanged();


                            }
                        }
                );

    }

    public void onPlay(View view) {
        if (RadioService.isPlaying() || MediaService.isPlaying())
            stopMedia();
        else
            playMedia();
    }

    public void onClickTitle(View view) {
        if(MediaService.isPlaying())
            startActivity(new Intent(this, MediaActivity.class));
        else
            startActivity(new Intent(this, NextActivity.class));
    }

    private void playMedia() {
        showLoading();
        startService(_radioService);
    }

    private void stopMedia() {
        RadioService.stop();
        MediaService.stop();
        stopService(_radioService);
        updatePlayerStatus();
    }


    Runnable runnable = new Runnable() {
        public void run() {
            updateState();
            updatePlayerStatus();
        }
    };

    private void updateState()
    {
        if (DataService.getDataTitle()!=null && !DataService.getDataTitle().title.equals("") && !MediaService.isPlaying())
        {

            mTrackArtist.setText(DataService.getDataTitle().artist);
            mTrack.setText(DataService.getDataTitle().track);
            if (!_lastCover.equals(DataService.getDataTitle().cover)) {

                mCover.setImageDrawable(imageThreadLoader.loadImage(DataService.getDataTitle().cover, new ImageThreadLoader.ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Drawable imageBitmap) {
                        mCover.setImageDrawable(imageBitmap);
                    }
                }));

            }
        } else if (MediaService.isPlaying()) {
            mTrackArtist.setText(MediaService.getDataTitle().artist);
            mTrack.setText(MediaService.getDataTitle().track);
            if (!_lastCover.equals(MediaService.getDataTitle().cover)) {

                mCover.setImageDrawable(imageThreadLoader.loadImage(MediaService.getDataTitle().cover, new ImageThreadLoader.ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Drawable imageBitmap) {
                        mCover.setImageDrawable(imageBitmap);
                    }
                }));

            }
        }

    }

    private void updatePlayerStatus() {
        if (RadioService.isPlaying() || MediaService.isPlaying()) {
            if (statusLoading())
                hideLoading();

            mPlayBtn.setImageResource(R.drawable.ic_media_pause);
        } else {
            mPlayBtn.setImageResource(R.drawable.ic_media_play);
        }

        if (RadioService.isErrors() || MediaService.isErrors()) {
            if (statusLoading())
                hideLoading();

            showErrorToast();

            RadioService.cleanErrors();
            MediaService.cleanErrors();
            stopMedia();
        }

    }

}
