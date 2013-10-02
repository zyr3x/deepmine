package com.deepmine.by;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.models.Blocks;
import com.deepmine.by.adapters.MultiSimpleAdapters;

public class MainActivity extends Activity {

    public static String TAG = "DEEPMINE";

    public static ImageView mPlayBtn;
    public static TextView mTrackTitle1;
    public static TextView mTrackTitle2;
    public static ListView mListView;

    private Intent radioService;

    private AQuery aq = new AQuery(this);

    MultiSimpleAdapters multiAdapter = new MultiSimpleAdapters();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        ResourceHelper.getInstance().init(this);

        mPlayBtn =(ImageView) findViewById(R.id.playBtn);
        mTrackTitle1 =  (TextView) findViewById(R.id.tackTitle1);
        mTrackTitle2 =  (TextView) findViewById(R.id.tackTitle2);
        mListView = (ListView) findViewById(R.id.listEvents);

        radioService = new Intent(this, RadioService.class);
        updateTitle();
        updateEvents();

    }

    protected void updateTitle()
    {
        Timer timer = new Timer();

        class UpdateTask extends TimerTask {

            public void run() {
                AQuery ajax = aq.ajax("http://deepmine.by/d/index.php/ajaxRadioTitle", JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json != null) {
                            try
                            {
                                String title = json.getString("title");
                                String artist = json.getString("artist");
                                String track = json.getString("track");

                                MainActivity.this.mTrackTitle1.setText(artist);
                                MainActivity.this.mTrackTitle2.setText(track);

                                aq.id(R.id.trackCover).image("http://deepmine.by/d/static/music/cover/"+title+".jpg",true, true, 0, R.drawable.ic_launcher_full);
                                updatePlayButton();
                            }
                            catch (JSONException e)
                            {
                                Log.d(TAG, "Exception parse");
                            }

                            Log.d(TAG, "JSON:" + json.toString());

                        } else {
                            Log.d(TAG, "Exception in ajax:" + status.getCode());

                        }
                    }
                });

            }
        }

        TimerTask updateTask = new UpdateTask();
        timer.scheduleAtFixedRate(updateTask, 0, 5000);
    }

    public void updateEvents()
    {
        AQuery ajax = aq.ajax("http://deepmine.by/android/hotlist", JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    try
                    {
                        Blocks blocks = new Blocks(json.getJSONArray("data"));

                        SimpleAdapter simpleAdapter = new SimpleAdapter( getApplicationContext(),
                                blocks.getList(),
                                R.layout.menu_row,
                                getResources().getStringArray(R.array.menu_row_element_names),
                                ResourceHelper.getInstance().getIntArray(R.array.menu_row_element_ids)
                        );

                        simpleAdapter.setViewBinder(new ItemImageBinder().init(
                                getApplicationContext(), R.drawable.ic_launcher));

                        mListView.setAdapter(simpleAdapter);
                        mListView.setDividerHeight(0);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int i, long l) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((TextView) view
                                    .findViewById(R.id.link)).getText().toString()));
                            startActivity(browserIntent);
                        }
                    });
                        simpleAdapter.notifyDataSetChanged();
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "Exception parse");
                    }

                    Log.d(TAG, "JSON:" + json.toString());

                } else {
                    Log.d(TAG, "Exception in ajax:" + status.getCode());

                }
            }
        });

    }



    public void onPlay(View view)
    {
        if(RadioService.isPlaying())
            stopMedia();
        else
            playMedia();
    }

    private void playMedia()
    {
        startService(radioService);
        updatePlayButton();
    }

    private void stopMedia()
    {
        stopService(radioService);
        RadioService.stop();
        updatePlayButton();
    }

    private void updatePlayButton()
    {
        if(RadioService.isPlaying())
            mPlayBtn.setImageResource(R.drawable.ic_media_pause);
        else
            mPlayBtn.setImageResource(R.drawable.ic_media_play);
    }




}
